package com.example.movie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class Movies {
    //영화 movie_no(pk), movie_title, movie_date(개봉일), movie_price, movie_rate, good_point_avg
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //사용해야할 DB가 db= postgreSQL라서 나중에 AUTO로 전환
    @Column(name = "movie_no")
    private Long movieNo;

    @Column(name = "movie_title", nullable = false)
    private String movieTitle;

    @Column(name = "movie_date")
    private String movieDate;

    @Column(name = "movie_rate")
    private String movieRate;

    @Column(name = "good_point_avg")
    private double goodPointAvg; // board 엔티티 goodPoint 값 평균값으로 구할거니 double타입

    @Column(length = 100000)
    private String img;

    private int price;

}
