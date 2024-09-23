package com.example.movie.repository;

import com.example.movie.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    // 전체 검색
    @Query(value = "select * from board order by board_id", nativeQuery = true)
    List<Board> searchQuery();

    // 영화제목 검색
    @Query(value = "SELECT * FROM board WHERE movie_no IN " +
            "(SELECT movie_no FROM movies WHERE movie_title LIKE CONCAT('%', :keyword, '%')) " +
            "ORDER BY board_id",
            countQuery = "SELECT count(*) FROM board WHERE movie_no IN " +
                    "(SELECT movie_no FROM movies WHERE movie_title LIKE CONCAT('%', :keyword, '%'))",
            nativeQuery = true)
    Page<Board> searchMovieTitle(@Param("keyword") String keyword, Pageable pageable);

    // 작성자 검색
    @Query(value = "SELECT * FROM board WHERE user_no IN " +
            "(SELECT user_no FROM users WHERE user_name LIKE CONCAT('%', :keyword, '%')) " +
            "ORDER BY board_id",
            countQuery = "SELECT count(*) FROM board WHERE user_no IN " +
                    "(SELECT user_no FROM users WHERE user_name LIKE CONCAT('%', :keyword, '%'))",
            nativeQuery = true)
    Page<Board> searchUser1(@Param("keyword") String keyword, Pageable pageable);
}