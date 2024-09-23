package com.example.movie.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalculateAge() {
        //given
        LocalDate birthDate = LocalDate.of(1995, 9, 11); // 예시 생년월일
        int age = userService.calculateAge(birthDate);
        assertEquals(29, age);  // 예상 결과와 비교 (2024년 기준)
    }

}