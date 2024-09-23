package com.example.movie.entity;

import com.example.movie.constant.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
//유저: user_no(pk), user_id. password, user_name, birth,phone, user_role,

    @Id
    @Column(name = "user_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //사용해야할 DB가 db= postgreSQL라서 나중에 AUTO로 전환
    private Long userNo;

    @Column(name = "user_id")
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(name = "user_name")
    private String userName;

    private String birth;

    private String email;

    private String phone;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private int money;
}
