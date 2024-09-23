package com.example.movie.api;
import com.example.movie.dto.UserDto;
import com.example.movie.service.BookService;
import com.example.movie.service.LocationService;
import com.example.movie.service.MovieService;
import com.example.movie.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api/cinema")
public class BookAPI {
    private final UserService userService;
    private final BookService bookService;

    public BookAPI(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }


    @PostMapping("/seat")
    public ResponseEntity<Map<String, Object>>  selectSeat(@RequestBody Map<String, Object> requestData) {
        // 요청 데이터 처리
        Long movieNo = Long.valueOf(requestData.get("movieNo").toString());
        Long locationNo = Long.valueOf(requestData.get("locationNo").toString());
        LocalDate date = LocalDate.parse(requestData.get("date").toString());
        List<List<Integer>> selectedSeats = (List<List<Integer>>) requestData.get("selectedSeats");
        int totalPrice = Integer.parseInt(requestData.get("totalPrice").toString());


        UserDto userDto = userService.loginUserInfo();
        boolean paymentSuccess = bookService.ticketPayment(userDto, totalPrice, movieNo, locationNo, date, selectedSeats);

        // 결과를 담을 Map
        Map<String, Object> response = new HashMap<>();

        if (paymentSuccess) {
            response.put("success", true);
            response.put("message", "예매되었습니다.");
        } else {
            response.put("success", false);
            response.put("message", "잔액이 부족합니다. 현재 충전 금액은: " + userDto.getMoney() + "입니다.");
        }
        return ResponseEntity.ok(response);  // 항상 200 OK로 반환
    }
}
