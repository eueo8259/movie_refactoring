package com.example.movie.controller;

import com.example.movie.config.PrincipalDetails;
import com.example.movie.dto.UserDto;
import com.example.movie.entity.Ticket;
import com.example.movie.entity.User;
import com.example.movie.service.BookService;
import com.example.movie.service.TicketService;
import com.example.movie.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("user")
@Slf4j
public class UserController {
    private final UserService userService;
    private final BookService bookService;
    private final TicketService ticketService;

    public UserController(UserService userService, BookService bookService, TicketService ticketService) {
        this.userService = userService;
        this.bookService = bookService;
        this.ticketService = ticketService;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("signup")
    public String singup(UserDto userDto,
                         Model model){
        model.addAttribute("maxDate", LocalDate.now().toString());
        return "user/signup";
    }

    @GetMapping("login")
    public String login(){
        return "user/login";
    }

    @GetMapping("information")
    public String information(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // 사용자의 이름 또는 ID 가져오기
            String username = authentication.getName();
            // 또는 PrincipalDetails로 형변환 후 사용자 정보 가져오기
            PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();

            // 여기서 userDetails에서 사용자 정보 추출
            User user = userDetails.getUser();

            UserDto userDto = userService.getOneUserDto(user.getUserNo());
            model.addAttribute("userDto", userDto);
        }
                return "user/user_information";
    }

    @GetMapping("ticket")
    public String ticket(Model model){

        UserDto userDto = userService.loginUserInfo();
        List<Ticket> ticketList = bookService.viewReservationDetails(userDto.getUserNo());
        model.addAttribute("ticketList", ticketList);
        return "user/user_ticket";
    }

    @GetMapping("delete")
    public String deleteUser(Model model){
        UserDto loginUserDto = userService.loginUserInfo();
        UserDto userDto = userService.getOneUserDto(loginUserDto.getUserNo());
        log.info("=========================================");
        log.info(userDto.toString());
        model.addAttribute("userDto", userDto);

        return "user/user_delete";
    }

    @GetMapping("money")
    public String moneyView(Model model) {
        UserDto loginUserDto = userService.loginUserInfo();

        UserDto userDto = userService.getOneUserDto(loginUserDto.getUserNo());
        log.info(userDto.toString());
        model.addAttribute("userDto", userDto);

        return "user/money";
    }
    @GetMapping("main")
    public String show(Model model){

        UserDto loginUserDto = userService.loginUserInfo();
        UserDto userDto = userService.getOneUserDto(loginUserDto.getUserNo());
        log.info(userDto.toString());
        model.addAttribute("userDto", userDto);

        return "user/user_main";
    }
}
