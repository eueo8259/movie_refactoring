package com.example.movie.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class SalesReportServiceTest {
    @InjectMocks
    private SalesReportService salesReportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void formattedSum() {
        //given
        Long amount = 1000L;
        String expected = "₩1,000";

        // When
        String actual = salesReportService.formattedSum(amount);

        // Then
        assertEquals(expected, actual, "금액 포맷이 올바르지 않습니다.");

    }
}