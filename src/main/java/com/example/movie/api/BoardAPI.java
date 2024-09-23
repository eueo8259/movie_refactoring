package com.example.movie.api;

import com.example.movie.dto.BoardDto;
import com.example.movie.dto.UserDto;
import com.example.movie.service.BoardService;
import com.example.movie.service.MovieService;
import com.example.movie.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/board")
public class BoardAPI {
    private final BoardService boardService;
    private final MovieService movieService;
    private final UserService userService;

    public BoardAPI(BoardService boardService, MovieService movieService, UserService userService) {
        this.boardService = boardService;
        this.movieService = movieService;
        this.userService = userService;
    }

    @DeleteMapping("delete")
    public ResponseEntity<String> boardDelete(@RequestBody Map<String, Long> boardId) {
        Long deleteId = boardId.get("deleteId");
        System.out.println(deleteId);
        try {
            boardService.delete(deleteId);
            return ResponseEntity.ok("사용자 정보가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 정보 삭제 실패했습니다.");
        }
    }

    @PostMapping("/insert")
    public String boardInsertView(@RequestBody BoardDto dto) {
        long movieNo = dto.getMovieNo();
        int goodPoint = dto.getGoodPoint();
        UserDto userDto = userService.loginUserInfo();
        dto.setUserNo(userDto.getUserNo());
        movieService.updateGoodPoint(movieNo,goodPoint);
        boardService.insert(dto);
        return "ok";
    }

    @PostMapping("update")
    public String boardUpdateView(@RequestBody BoardDto boardDto) {
        movieService.updateGoodPoint(boardDto.getMovieNo(), boardDto.getGoodPoint());
        boardService.update(boardDto);
        return "ok";
    }



}
