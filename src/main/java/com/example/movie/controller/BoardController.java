package com.example.movie.controller;

import com.example.movie.config.PrincipalDetails;
import com.example.movie.dto.BoardDto;
import com.example.movie.dto.MoviesDto;
import com.example.movie.dto.UserDto;
import com.example.movie.entity.Board;
import com.example.movie.entity.User;
import com.example.movie.repository.BoardRepository;
import com.example.movie.service.BoardService;
import com.example.movie.service.MovieService;
import com.example.movie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("board")
@Slf4j
public class BoardController {
    private final BoardRepository boardRepository;
    private final BoardService boardService;
    private final MovieService movieService;
    private final UserService userService;

    public BoardController(BoardRepository boardRepository, BoardService boardService, MovieService movieService, UserService userService) {
        this.boardRepository = boardRepository;
        this.boardService = boardService;
        this.movieService = movieService;
        this.userService = userService;
    }

    @GetMapping("list")
    public String boardMainList(Model model,
                                @PageableDefault(page = 0, size = 10, sort = "boardId",
                                direction = Sort.Direction.DESC) Pageable pageable){
        Page<Board> boardPage = boardService.viewBoard(pageable);
        int totalPage = boardPage.getTotalPages();
        List<Integer> barNumbers = boardService.getPaginationBarNumbers(
                pageable.getPageNumber(), totalPage);
        model.addAttribute("pagination", barNumbers);
        model.addAttribute("paging", boardPage);
        boolean flag = true;
        model.addAttribute("flag", flag);
        UserDto userDto = userService.loginUserInfo();
        model.addAttribute("userNo", userDto.getUserNo());

        return "board/list";
    }

    @GetMapping("insert")
    public String boardInsertForm(Model model) {
        UserDto userDto = userService.loginUserInfo();

        model.addAttribute("username", userDto.getUserName());

        List<MoviesDto> moviesDtoList = movieService.getAllMovies();
        model.addAttribute("moviesDtoList", moviesDtoList);
        model.addAttribute("boardDto", new BoardDto());
        return "board/insert";
    }

    @GetMapping("update")
    public String boardUpdateForm(@RequestParam("updateId") Long boardId,
                                      Model model) {
        BoardDto boardDto = boardService.getOneBoard(boardId);
        List<MoviesDto> moviesDtoList = movieService.getAllMovies();
        model.addAttribute("boardDto", boardDto);
        model.addAttribute("moviesDtoList", moviesDtoList);
        return "board/update";
    }

    @GetMapping("/search")
    public String searchBoard(@RequestParam("type")String type,
                              @RequestParam("keyword")String keyword,
                              @PageableDefault(page = 0, size = 10, sort = "board_id",
                                      direction = Sort.Direction.DESC) Pageable pageable,
                              Model model) {
        List<BoardDto> boardDtoList = new ArrayList<>();
        boolean flag = false;

        switch (type) {
            case "movieNo":
                // 영화제목 DB 검색
                Page<Board> boardPage = boardService.viewBoard1(keyword,pageable);
                int totalPage = boardPage.getTotalPages();
                List<Integer> barNumbers = boardService.getPaginationBarNumbers(
                        pageable.getPageNumber(), totalPage);
                model.addAttribute("pagination", barNumbers);
                model.addAttribute("paging", boardPage);
                model.addAttribute("flag", flag);
                model.addAttribute("type", type);
                model.addAttribute("keyword", keyword);
                break;
            case "userNo":
                // 작성자로 DB 검색
                Page<Board> boardPage1 = boardService.viewBoard2(keyword,pageable);
                int totalPage1 = boardPage1.getTotalPages();
                List<Integer> barNumbers1 = boardService.getPaginationBarNumbers(
                        pageable.getPageNumber(), totalPage1);
                model.addAttribute("pagination", barNumbers1);
                model.addAttribute("paging", boardPage1);
                model.addAttribute("flag", flag);
                model.addAttribute("type", type);
                model.addAttribute("keyword", keyword);
                String l =keyword;

                break;
            default:
                // 전체 검색
                boardDtoList = boardRepository.searchQuery()
                        .stream()
                        .map(x -> BoardDto.fromBoardEntity(x))
                        .toList();
                break;
        }
        UserDto userDto = userService.loginUserInfo();
        model.addAttribute("userNo", userDto.getUserNo());
        return "board/list";
    }
}
