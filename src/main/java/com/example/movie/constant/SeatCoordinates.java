package com.example.movie.constant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatCoordinates {
    private int row;
    private int column;

    public SeatCoordinates(int row, int column) {
        this.row = row;
        this.column = column;
    }
}
