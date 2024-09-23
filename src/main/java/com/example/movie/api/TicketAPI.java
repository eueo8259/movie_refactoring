package com.example.movie.api;

import com.example.movie.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/ticket")
public class TicketAPI {
    private final TicketService ticketService;

    public TicketAPI(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @DeleteMapping("delete")
    public ResponseEntity<String> boardDelete(@RequestBody Map<String, Long> ticketNo) {
        Long deleteId = ticketNo.get("deleteId");
        System.out.println(deleteId);
        try {
            ticketService.ticketCancel(deleteId);
            return ResponseEntity.ok("사용자 정보가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 정보 삭제 실패했습니다.");
        }
    }

}
