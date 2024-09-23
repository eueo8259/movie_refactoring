package com.example.movie.api;

import com.example.movie.dto.UserDto;
import com.example.movie.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/admin/delete")
public class AdminAPI {

    private final UserService userService;

    public AdminAPI(UserService userService) {
        this.userService = userService;
    }
    @DeleteMapping("delete")
    public ResponseEntity<String> userDelete(@RequestBody Map<String, Long> payload) {
        Long deleteId = payload.get("deleteId");
        System.out.println(deleteId);
        try {
            userService.deleteUser(deleteId);
            return ResponseEntity.ok("사용자 정보가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자 정보 삭제 실패했습니다.");
        }
    }


}
