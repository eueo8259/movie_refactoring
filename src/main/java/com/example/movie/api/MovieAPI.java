package com.example.movie.api;

import com.example.movie.dto.MoviesDto;
import com.example.movie.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/movie")
public class MovieAPI {
    private final MovieService movieService;

    public MovieAPI(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/insert")
    public ResponseEntity<String> movieInsert(@RequestBody MoviesDto moviesDto) {
        try {
            movieService.insert(moviesDto);
            return ResponseEntity.ok("영화가 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("영화 등록에 실패하였습니다.");
        }

    }
    @PostMapping("/update")
    public ResponseEntity<String> movieUpdate(@RequestBody MoviesDto moviesDto) {
        try {
            movieService.update(moviesDto);
            return ResponseEntity.ok("영화 정보가 성공적으로 업데이트되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("영화 정보 업데이트에 실패했습니다.");
        }
    }
    @DeleteMapping("delete")
    public ResponseEntity<String> userDelete(@RequestBody Map<String, Long> id) {
        Long deleteId = id.get("deleteId");
        System.out.println(deleteId);
        try {
            movieService.deleteMovie(deleteId);
            return ResponseEntity.ok("영화 정보가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("영화 정보 삭제 실패했습니다.");
        }
    }
}
