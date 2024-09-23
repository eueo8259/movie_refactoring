package com.example.movie.service;

import com.example.movie.dto.BoardDto;
import com.example.movie.entity.Board;
import com.example.movie.entity.Movies;
import com.example.movie.entity.User;
import com.example.movie.repository.BoardRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Page<BoardDto> viewAllBoard(Pageable pageable) {
        return boardRepository.findAll(pageable)
                .map(BoardDto::fromBoardEntity);
    }

    public Page<Board> viewBoard(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    public Page<Board> viewBoard1(String keyword, Pageable pageable) {
        return boardRepository.searchMovieTitle(keyword, pageable);
    }

    public Page<Board> viewBoard2(String keyword, Pageable pageable) {
        return boardRepository.searchUser1(keyword, pageable);
    }

    public void insert(BoardDto dto) {

        Movies movies = em.find(Movies.class, dto.getMovieNo());
        User user = em.find(User.class, dto.getUserNo());
        Board board = Board.builder()
                .boardId(dto.getBoardId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .movies(movies)
                .user(user)
                .goodPoint(dto.getGoodPoint())
                .build();
        boardRepository.save(board);
    }

    public void update(BoardDto boardDto) {
        Board board = boardRepository.findById(boardDto.getBoardId()).orElse(null);
        if (board != null) {
            board.setTitle(boardDto.getTitle());
            board.setContent(boardDto.getContent());
            board.setGoodPoint(boardDto.getGoodPoint());
            boardRepository.save(board);
        }
    }

    @Transactional
    public void delete(Long id) {
        Board board = em.find(Board.class, id);
        board.setMovies(null);
        board.setUser(null);
        em.remove(board);
    }

    public void deleteBoardByUser(User user) {
        String boardQuery = "DELETE FROM Board b WHERE b.user = :user";
        em.createQuery(boardQuery)
                .setParameter("user", user)
                .executeUpdate();
    }


    public List<BoardDto> findAll() {
        List<BoardDto> boardDtoList = new ArrayList<>();
        return boardRepository.findAll()
                .stream()
                .map(x -> BoardDto.fromBoardEntity(x))
                .toList();
    }

    @Autowired
    EntityManager em;

    public List<Board> findAllem() {
        String sql = "SELECT b FROM Board b";
        Query query = em.createQuery(sql);
        List<Board> boardList = query.getResultList();
        return boardList;
    }

    public Page<Board> pageList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }
    private static final int BAR_LENGTH = 5;

    public List<Integer> getPaginationBarNumbers(int pageNumber, int totalPage) {
        int startNumber = Math.max(pageNumber - (BAR_LENGTH / 2), 0);

        int endNumber = Math.min(startNumber + BAR_LENGTH, totalPage);

        return IntStream.range(startNumber, endNumber).boxed().toList();
    }

    public BoardDto getOneBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .map(x -> BoardDto.fromBoardEntity(x))
                .orElse(null);
    }

    public List<Board> movie() {
        String sql = "SELECT b.movieTitle  FROM Board b";
        TypedQuery<Board> query = em.createQuery(sql, Board.class);
        List<Board> movie = query.getResultList();
        return movie;
    }

    public List<Board> title(Long movieNo) {
        String sql = "SELECT b FROM Board b WHERE b.movies.movieNo = :movieNo";
        TypedQuery<Board> query = em.createQuery(sql, Board.class).setParameter("movieNo", movieNo);
        return query.getResultList();
    }
}
