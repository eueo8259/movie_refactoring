package com.example.movie.dto;

import com.example.movie.entity.Board;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BoardDto {
    private Long boardId;
    private String title;
    private String content;
    private int goodPoint;
    private Long movieNo;
    private Long userNo;

    public static BoardDto fromBoardEntity(Board board) {
        return new BoardDto(
                board.getBoardId(),
                board.getTitle(),
                board.getContent(),
                board.getGoodPoint(),
                board.getMovies().getMovieNo(),
                board.getUser().getUserNo()
        );
    }
}
