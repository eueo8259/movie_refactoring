package com.example.movie.controller;

import com.example.movie.dto.MoviesDto;
import com.example.movie.dto.TicketDto;
import com.example.movie.dto.TotalPriceDto;
import com.example.movie.dto.UserDto;
import com.example.movie.entity.Board;
import com.example.movie.entity.Movies;
import com.example.movie.entity.Ticket;
import com.example.movie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("admin")
public class AdminController {
//    @Autowired
//    EntityManager em;
//    private String uploadDir;
    private final UserService userService;
    private final MovieService movieService;
    private final BoardService boardService;
    private final BookService bookService;
    private final SalesReportService salesReportService;
    private final TicketService ticketService;

    public AdminController(UserService userService, MovieService movieService, BoardService boardService, BookService bookService, SalesReportService salesReportService, TicketService ticketService) {
        this.userService = userService;
        this.movieService = movieService;
        this.boardService = boardService;
        this.bookService = bookService;
        this.salesReportService = salesReportService;
        this.ticketService = ticketService;
    }

    @GetMapping("")
//    관리자 페이지 메인 화면, 쿼리 사용 인기 영화 티켓판매
    public String adminView(Model model) {
        List<Movies> moviesList = movieService.GoodMovie();
        model.addAttribute("movie",moviesList);

        List<Movies> movies = movieService.TicketMovie();
        model.addAttribute("sell",movies);
        return "admin/main";
    }

    @GetMapping("user")
//    관리자페이지 회원관리 화면
    public String userView(Model model) {
        List<UserDto> userDtoList = userService.findAll();
        model.addAttribute("user", userDtoList);
        return "admin/user";
    }

    @GetMapping("update")
//    관리자페이지 회원 수정화면
    public String updateView(@RequestParam("updateId") Long userNo,
                             Model model) {
        UserDto userDto = userService.getOneUserDto(userNo);
        model.addAttribute("userDto", userDto);
//        생년월일 현재 날짜 이후로 선택 불가능
        model.addAttribute("maxDate", LocalDate.now().toString());
        return "admin/user_update";
    }

    @GetMapping("movie")
//    관리자페이지 영화관리 화면, 게시판 별점 바로 반영
    public String movieView(Model model) {
        List<MoviesDto> moviesDtoList = movieService.findAll();
        model.addAttribute("movie", moviesDtoList);
        return "admin/movie";
    }


    @GetMapping("movie_update")
//    관리자페이지 영화 수정화면
    public String movieUpdateView(@RequestParam("updateId") Long movieNo,
                                  Model model) {
        MoviesDto moviesDto = movieService.getOneMovieDto(movieNo);
        model.addAttribute("movie", moviesDto);
        return "admin/movie_update";
    }

    @GetMapping("insert")
//    관리자페이지 영화 등록 화면
    public String insertMovie(Model model) {
        model.addAttribute("movie", new MoviesDto());
        return "admin/movie_insert";
    }

    @GetMapping("board")
    public String board(Model model,
                        @PageableDefault(page = 0, size = 10, sort = "boardId",
                                direction = Sort.Direction.DESC) Pageable pageable) {
        //       넘겨온 페이지 번호로 리스트 받아오기
        Page<Board> boardPage = boardService.pageList(pageable);

        //        페이지 블럭처리
        int totalPage = boardPage.getTotalPages();
        List<Integer> barNumbers = boardService.getPaginationBarNumbers(
                pageable.getPageNumber(), totalPage);
        model.addAttribute("pagination", barNumbers);
        model.addAttribute("paging", boardPage);
        return "admin/board";
    }

    @GetMapping("ticket")
    public String ticket(Model model) {
        List<Ticket> ticketDtoList = ticketService.viewTicketList();
        model.addAttribute("ticketList", ticketDtoList);
        return "admin/ticket";
    }

    @GetMapping("total")
    public String totalMoney(Model model){
        List<TicketDto> ticketDtoList = ticketService.findAll();
        log.info(ticketDtoList.toString());
        model.addAttribute("ticket",ticketDtoList);

        Long sum = salesReportService.All();

        String formattedSum = salesReportService.formattedSum(sum);


        model.addAttribute("formattedSum", formattedSum);
        return "admin/total_money";
    }
    @GetMapping("total_movie")
    public String totalMovie(Model model){
        List<TotalPriceDto> ticketName = salesReportService.findMovieTotalPrice();
        model.addAttribute("ticket",ticketName);

        Long sum = salesReportService.SumMovie();
        String formattedSum = salesReportService.formattedSum(sum);
        model.addAttribute("formattedSum", formattedSum);
        return "admin/total/movie";
    }

    @GetMapping("total_name")
    public String totalName(Model model) {
        // 해당 사용자의 매출 정보를 가져옵니다.
        List<TotalPriceDto> ticketName = salesReportService.findUserTotalPrice();
        model.addAttribute("ticket", ticketName);

        // 전체 매출 총액을 가져옵니다.
        Long sum = salesReportService.SumUser();
        String formattedSum = salesReportService.formattedSum(sum);
        model.addAttribute("formattedSum", formattedSum);

        return "admin/total/name";
    }

    @GetMapping("total_location")
    public String totalLocation(Model model){
        List<TotalPriceDto> ticketName = salesReportService.findLocationTotalPrice();
        model.addAttribute("ticket",ticketName);

        Long sum = salesReportService.SumLocation();
        String formattedSum = salesReportService.formattedSum(sum);

        model.addAttribute("formattedSum", formattedSum);
        return "admin/total/location";
    }

    @GetMapping("total_date")
    public String totalDate(Model model){
        List<TotalPriceDto> ticketName = salesReportService.findDateTotalPrice();
        model.addAttribute("ticket",ticketName);

        Long sum = salesReportService.SumDate();
        String formattedSum = salesReportService.formattedSum(sum);

        model.addAttribute("formattedSum", formattedSum);
        return "admin/total/date";
    }
}
