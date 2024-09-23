package com.example.movie.controller;

import com.example.movie.dto.*;
import com.example.movie.service.BookService;
import com.example.movie.service.LocationService;
import com.example.movie.service.MovieService;
import com.example.movie.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("cinema")
@Slf4j
public class BookController {

    private final UserService userService;
    private final LocationService locationService;
    private final MovieService movieService;
    private final BookService bookService;

    public BookController(UserService userService, LocationService locationService, MovieService movieService, BookService bookService) {
        this.userService = userService;
        this.locationService = locationService;
        this.movieService = movieService;
        this.bookService = bookService;
    }

    //GetMapping의 기능은 화면 출력만. (출력에 필요한 model.addAttribute하는 거 포함)
    @GetMapping("theater")
    public String selectTheaterView(Model model){

        //티켓에 필요한 날짜 계산하는 기능은 서비스로 빼고 그 서비스에서 따로 사용하는 걸로
        List<LocalDate> dateList = bookService.ticketDate();
        model.addAttribute("dateList", dateList);

        //유저의 나이 체크하는 기능 유저 서비스로 뺌(유저의 상태(나이)를 체크하는 거니까 유저서비스가 맞는 거 같음)
        int userAge = userService.userAgeCheck();
        model.addAttribute("years", userAge);

        List<LocationDto> locationDtoList = locationService.findAllLocationDto();
        model.addAttribute("locationDtoList", locationDtoList);

        List<MoviesDto> moviesDtoList = movieService.findAll();
        model.addAttribute("moviesDtoList", moviesDtoList);
        return "/book/select_theater";
    }

    @GetMapping("/seat")
    public String selectSeatView(@RequestParam("movie") Long movieNo,
                                 @RequestParam("location") Long locationNo,
                                 @RequestParam("date") LocalDate date,
                                 Model model){
        MoviesDto moviesDto = movieService.getOneMovieDto(movieNo);
        model.addAttribute("moviesDto", moviesDto);
        model.addAttribute("locationNo", locationNo);
        model.addAttribute("date", date);

        List<SeatDto> seatDtoList = bookService.searchSeatByMovieLocationAndDate(movieNo, locationNo, date);
        boolean[][] seats = bookService.convertToSeatArray(seatDtoList);

        // 모델에 좌석 배열을 추가
        model.addAttribute("seats", seats);
        return "/book/select_seat";
    }

}
