package com.example.movie.service;

import com.example.movie.repository.SeatRepository;
import com.example.movie.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void ticketDate() {
        // Given: 오늘 날짜를 기준으로 7일 간의 날짜를 계산해야 한다.
        LocalDate today = LocalDate.now();

        // When: ticketDate 메서드를 호출한다.
        List<LocalDate> dateList = bookService.ticketDate();

        // Then: 반환된 날짜 리스트의 크기가 7이고, 각 날짜가 올바른지 검증한다.
        assertEquals(7, dateList.size(), "날짜 리스트 크기가 7이어야 합니다.");

        for (int i = 0; i < 7; i++) {
            assertEquals(today.plusDays(i), dateList.get(i), "각 날짜가 예상 날짜와 일치해야 합니다.");
        }

    }
}