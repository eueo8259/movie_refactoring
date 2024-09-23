package com.example.movie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Board {
    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY ) //사용해야할 DB가 db= postgreSQL라서  나중에 AUTO로 전환
    private Long boardId;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String content;

    @Column(name = "good_point",nullable = false)
    private int goodPoint;

    @ManyToOne
    @JoinColumn(name = "movie_no", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Movies movies;

    @ManyToOne
    @JoinColumn(name = "user_no", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

}
