package com.example.movie.dto;

import com.example.movie.constant.UserRole;
import com.example.movie.entity.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {

    private Long userNo;

    @NotEmpty(message = "사용자 ID는 필수입니다.")
    private String userId;

    @NotEmpty(message = "비밀번호는 필수 입니다")
    private  String password1;

    @NotEmpty(message = "비밀번호 확인은 필수 입니다")
    private String password2;

    @NotEmpty(message = "이름은 필수 입니다")
    private String userName;

    @NotEmpty(message = "생년월일은 필수 입니다")
    private String birth;

    @NotEmpty(message = "이메일은 필수 입니다")
    private String email;

    private String phone;

    private int money;

    private UserRole userRole;

    public UserDto(Long userNo, String userId, String password1, String userName, String birth,String email, String phone, int money,UserRole userRole) {
        this.userNo = userNo;
        this.userId = userId;
        this.password1 = password1;
        this.userName = userName;
        this.birth = birth;
        this.email = email;
        this.phone = phone;
        this.money = money;
        this.userRole = userRole;
    }

    public  static  UserDto fromEntity(User user){
        return new UserDto(
                user.getUserNo(),
                user.getUserId(),
                user.getPassword(),
                user.getUserName(),
                user.getBirth(),
                user.getEmail(),
                user.getPhone(),
                user.getMoney(),
                user.getUserRole()
        );
    }
}
