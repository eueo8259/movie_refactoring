package com.example.movie.dto;

import com.example.movie.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDto {
    private Long ticketNo;
    private String  movieNo;
    private String userNo;
    private Long seatId;
    private String locationNo;
    private LocalDate bookDate; //예약 날짜 (영화보는 날짜)
    private int totalPrice;

    public TicketDto(Long ticketNo, String movieTitle, String userName, String locationName, LocalDate bookDate, int totalPrice) {
        this.ticketNo = ticketNo;
        this.movieNo = movieTitle;
        this.userNo = userName;
        this.locationNo = locationName;
        this.bookDate = bookDate;
        this.totalPrice = totalPrice;
    }

    public static TicketDto fromTicketEntity(Ticket ticket){
        return new TicketDto(
                ticket.getTicketNo(),
                ticket.getMovies().getMovieTitle(),
                ticket.getUser().getUserName(),
                ticket.getLocation().getLocationName(),
                ticket.getBookDate(),
                ticket.getTotalPrice()
        );
    }
}
