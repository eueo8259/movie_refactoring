package com.example.movie.service;

import com.example.movie.dto.MoviesDto;
import com.example.movie.entity.Board;
import com.example.movie.entity.Movies;
import com.example.movie.entity.Ticket;
import com.example.movie.repository.BoardRepository;
import com.example.movie.repository.MoviesRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {
    @Autowired
    EntityManager em;
    
    private final MoviesRepository moviesRepository;

    public MovieService(MoviesRepository moviesRepository, BoardRepository boardRepository) {
        this.moviesRepository = moviesRepository;
    }

    public List<MoviesDto> findAll() {
        return moviesRepository.findAll()
                .stream()
                .map(x->MoviesDto.fromMoviesEntity(x))
                .toList();
    }

    public Movies updateGoodPoint(Long movieNo, double goodPoint) {
        // 평균 평점 계산 쿼리
        Double avgGoodPoint = getAvgGoodPoint(movieNo, goodPoint);
        // 영화 정보 가져오기 쿼리
        Movies movies = getOneMovie(movieNo);
        movies.setGoodPointAvg(avgGoodPoint);
        return movies;
    }

    private Double getAvgGoodPoint(Long movieNo, double goodPoint) {
        String sql1 = "SELECT AVG(b.goodPoint) FROM Board b WHERE b.movies.movieNo = :movieNo";
        TypedQuery<Double> query1 = em.createQuery(sql1, Double.class).setParameter("movieNo", movieNo);
        Double avgGoodPoint = null;
        try {
            avgGoodPoint = query1.getSingleResult();
        } catch (NoResultException e) {
            avgGoodPoint = 0.0; // 결과가 없는 경우 기본값 설정
        }
        if (avgGoodPoint == null) {
            avgGoodPoint = goodPoint; // null인 경우 기본값 설정
        }
        return avgGoodPoint;
    }


    public MoviesDto getOneMovieDto(Long movieNo) {
        Movies movies = getOneMovie(movieNo);
        return MoviesDto.fromMoviesEntity(movies);
    }

    public Movies getOneMovie(Long movieNo){
        return moviesRepository.findById(movieNo).orElse(null);
    }

//    @Transactional
//    public void delete(Long movieNo) {
//        Movies movies = em.find(Movies.class, movieNo);
//
//        String sql1 = "SELECT b FROM Board b WHERE b.movies.movieNo=:movieNo";
//        TypedQuery<Board> query1 = em.createQuery(sql1, Board.class)
//                .setParameter("movieNo", movieNo);
//        List<Board> boardList1 = query1.getResultList();
//        for (Board board : boardList1){
//            board.setMovies(null);
//        }
//        String sql2 = "SELECT t FROM Ticket t WHERE t.movies.movieNo=:movieNo";
//        TypedQuery<Ticket> query2 = em.createQuery(sql2, Ticket.class)
//                .setParameter("movieNo", movieNo);
//        List<Ticket> boardList2 = query2.getResultList();
//        for (Ticket ticket : boardList2){
//            ticket.setMovies(null);
//        }
//        em.remove(movies);
//    }
    @Transactional
    public void deleteMovie(Long movieNo) {
        Movies movies = getOneMovie(movieNo);
        // 1. 영화 좌석 정보 삭제
        deleteSeatsByMovie(movies);
        // 2. 영화 티켓 정보 삭제
        deleteTicketByMovie(movies);
        // 3. 영화 게시물 삭제
        deleteBoardByMovie(movies);
        // 4. 영화 삭제
        em.remove(movies);
    }

    public void deleteSeatsByMovie(Movies movies){
        String seatQuery = "DELETE FROM Seat s WHERE s.ticket.movies = :movie";
        em.createQuery(seatQuery)
                .setParameter("movie", movies)
                .executeUpdate();
    }

    public void deleteTicketByMovie(Movies movies){
        String ticketQuery = "DELETE FROM Ticket t WHERE t.movies = :movie";
        em.createQuery(ticketQuery)
                .setParameter("movie", movies)
                .executeUpdate();
    }

    public void deleteBoardByMovie(Movies movies){
        String boardQuery = "DELETE FROM Board b WHERE b.movies = :movie";
        em.createQuery(boardQuery)
                .setParameter("movie", movies)
                .executeUpdate();
    }

    @Transactional
    public void update(MoviesDto moviesDto) {
        Movies movies = getOneMovie(moviesDto.getMovieNo());
        movies.setMovieTitle(moviesDto.getMovieTitle());
        movies.setMovieDate(moviesDto.getMovieDate());
        movies.setMovieRate(moviesDto.getMovieRate());
        movies.setPrice(moviesDto.getPrice());
        if (moviesDto.getImg() != null) {
            movies.setImg(moviesDto.getImg());
        }
        movies.setGoodPointAvg(movies.getGoodPointAvg());
        em.merge(movies);
    }

    public List<MoviesDto> getAllMovies() {
        List<MoviesDto> moviesDtoList = new ArrayList<>();
        moviesRepository.findAll().forEach(
                movies -> moviesDtoList.add(MoviesDto.fromMoviesEntity(movies))
        );
        return moviesDtoList;
    }

    public void insert(MoviesDto dto) {
        Movies movies = Movies.builder()
                .movieTitle(dto.getMovieTitle())
                .movieDate(dto.getMovieDate())
                .movieRate(dto.getMovieRate())
                .img(dto.getImg())
                .price(dto.getPrice())
                .build();
        moviesRepository.save(movies);
    }


// 포스트그레에서 원래 사용하던 코드
//    public List<Movies> RandomMovie() {
//        String sql = "SELECT m FROM Movies m ORDER BY RANDOM()";
//        TypedQuery<Movies> query = em.createQuery(sql, Movies.class).setMaxResults(3);
//        return query.getResultList();
//    }

//mysql로 처리하면서 sql구문 오류 새롭게 처리한 코드
    public List<Movies> RandomMovie() {
        String sql = "SELECT * FROM movies ORDER BY RAND() LIMIT 3";
        Query query = em.createNativeQuery(sql, Movies.class);
        return query.getResultList();
    }

    public List<Movies> GoodMovie() {
        String sql = "SELECT m FROM Movies m ORDER BY m.goodPointAvg DESC LIMIT 3";
        TypedQuery<Movies> query = em.createQuery(sql, Movies.class);
        return query.getResultList();
    }

    public List<Movies> TicketMovie() {
        String sql = "SELECT t.movies FROM Ticket t GROUP BY t.movies " +
                "ORDER BY SUM(t.totalPrice) DESC";
        TypedQuery<Movies> query = em.createQuery(sql, Movies.class);
        query.setMaxResults(3);
        return query.getResultList();
    }
}
