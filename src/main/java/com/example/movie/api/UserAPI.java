package com.example.movie.api;

import com.example.movie.dto.UserDto;
import com.example.movie.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/user")
public class UserAPI {
    private final UserService userService;

    public UserAPI(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody UserDto userDto) {
        // 사용자 ID 중복 체크
        boolean checkUser = userService.checkId(userDto.getUserId());
        if (checkUser) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 등록된 사용자입니다");
        }

        // 비밀번호 확인
        if (!userDto.getPassword1().equals(userDto.getPassword2())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("2개의 패스워드가 일치하지 않습니다.");
        }

        // 사용자 생성
        userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }

    @PostMapping("update")
    public ResponseEntity<String> userUpdate(@RequestBody UserDto userDto) {

        try {
            userService.update(userDto);
            return ResponseEntity.ok("사용자 정보가 성공적으로 업데이트되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 정보 업데이트에 실패했습니다.");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestBody Map<String, Object> requestBody) {
        Long userNo = Long.valueOf((String) requestBody.get("userNo"));
        String inputPassword = (String) requestBody.get("password");

        UserDto userDto = userService.getOneUserDto(userNo);

        if (!passwordEncoder.matches(inputPassword, userDto.getPassword1())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("비밀번호를 틀리셨습니다.");
        } else {
            try {
                userService.deleteUser(userNo);
                return ResponseEntity.ok("삭제 되었습니다.");
            } catch (Exception e) {
                // 예외 발생 시 처리
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 중 오류가 발생했습니다.");
            }
        }
    }
    @PostMapping("money")
    public ResponseEntity<String> moneyInsert(@RequestBody Map<String, Object> requestBody) {
        UserDto userDto = userService.loginUserInfo();

        // 요청에서 비밀번호와 충전 금액을 가져오기
        String inputPassword = (String) requestBody.get("password");
        int money = Integer.valueOf((String) requestBody.get("money"));

        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자 정보가 없습니다.");
        }

        if (!passwordEncoder.matches(inputPassword, userDto.getPassword1())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("비밀번호가 틀렸습니다.");
        }

        // 충전 처리
        userService.money(userDto, money);
        return ResponseEntity.ok().body("충전 완료");
    }
}
