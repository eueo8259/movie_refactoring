package com.example.movie.controller;

import com.example.movie.dto.LocationDto;
import com.example.movie.dto.MoviesDto;
import com.example.movie.entity.Board;
import com.example.movie.entity.Movies;
import com.example.movie.service.BoardService;
import com.example.movie.service.LocationService;
import com.example.movie.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("cinema")
public class MainController {

    private final BoardService boardService;
    private final LocationService locationService;
    private final MovieService movieService;

    public MainController(BoardService boardService, LocationService locationService, MovieService movieService) {
        this.boardService = boardService;
        this.locationService = locationService;
        this.movieService = movieService;
    }


    @GetMapping("")
    public String mainView(Model model){
//        메인페이지에서 랜덤 영화 3개 출력
        List<Movies> moviesList = movieService.RandomMovie();
        model.addAttribute("movie",moviesList);
        return "cinema/cinema_main";
    }

    @GetMapping("movie")
//    메인페이지에서 영화목록 출력
    public String movie(Model model){
        List<MoviesDto> moviesDtoList = movieService.findAll();
        log.info(moviesDtoList.toString());
        model.addAttribute("movie",moviesDtoList);
        return "show_movie";
    }


    @GetMapping("board")
//    관람후기 페이지로 이동
    public String board(@RequestParam("movieNo")Long movieNo,
                        Model model){
        List<Board> board = boardService.title(movieNo);
        model.addAttribute("board",board);
        return "show_board";
    }



    @GetMapping("location")
//    메인페이지에서 지점페이지로 이동
    public String location(Model model){
        List<LocationDto> locationDtoList = locationService.findAllLocationDto();
        model.addAttribute("location",locationDtoList);
        return "show_location";
    }
}
