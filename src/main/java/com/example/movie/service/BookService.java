package com.example.movie.service;

import com.example.movie.constant.SeatCoordinates;
import com.example.movie.dto.SeatDto;
import com.example.movie.dto.UserDto;
import com.example.movie.entity.*;
import com.example.movie.repository.SeatRepository;
import com.example.movie.repository.TicketRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    EntityManager em;
    private final UserService userService;
    private final MovieService movieService;
    private final LocationService locationService;
    private final TicketService ticketService;

    public BookService(TicketRepository ticketRepository, SeatRepository seatRepository, UserService userService, MovieService movieService, LocationService locationService, TicketService ticketService) {
        this.userService = userService;
        this.movieService = movieService;
        this.locationService = locationService;
        this.ticketService = ticketService;
    }

    public List<LocalDate> ticketDate(){
        LocalDate today = LocalDate.now();
        List<LocalDate> dateList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.plusDays(i);
            dateList.add(date);
        }
        return dateList;
    }

    public boolean[][] convertToSeatArray(List<SeatDto> seatDtoList) {

        int numRows = 6;
        int numColumns = 8;

        // 2차원 배열 생성
        boolean[][] seat = new boolean[numRows][numColumns];

        // 모든 좌석을 사용 가능 상태로 초기화 (true로 설정)
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                seat[i][j] = true;  // 모든 좌석을 사용 가능(true) 상태로 초기화
            }
        }

        // seatDtoList의 각 SeatDto를 순회하면서 실제 사용 중인 좌석을 false로 설정
        for (SeatDto seatDto : seatDtoList) {
            int row = seatDto.getSeatRowNo();
            int column = seatDto.getSeatColumnNo();

            // 좌석이 사용 중인 경우 false로 설정
            seat[row][column] = false;
        }

        return seat;
    }
    @Transactional
    public boolean ticketPayment(UserDto userDto, int totalPrice, Long movieNo, Long locationNo, LocalDate date,
                                 List<List<Integer>> selectedSeats){
        if (userDto.getMoney() < totalPrice){
            return false;
        } else {
            int leftMoney = userDto.getMoney() - totalPrice;
            userService.leftMoney(userDto.getUserNo(), leftMoney);
            ticketBookService(movieNo, locationNo, userDto.getUserNo(),date, selectedSeats, totalPrice);
            return true;
        }
    }

    //JPQL
    public List<SeatDto> searchSeatByMovieLocationAndDate(Long movieNo, Long locationNo, LocalDate date) {
        String sql = "SELECT s FROM Seat s WHERE s.ticket.movies.movieNo = :movieNo " +
                "AND s.ticket.location.locationNo = :locationNo AND s.ticket.bookDate = :date";
        TypedQuery<Seat> query = em.createQuery(sql, Seat.class)
                .setParameter("movieNo", movieNo)
                .setParameter("locationNo", locationNo)
                .setParameter("date", date);
        List<Seat> seatList = query.getResultList();

        return seatList.stream()
                .map(x -> SeatDto.fromSeatEntity(x))
                .toList();
    }

    @Transactional
    public List<Ticket> viewReservationDetails(Long userNo) {

        String sql = "SELECT t FROM Ticket t WHERE t.user.userNo = :userNo";
        TypedQuery<Ticket> query = em.createQuery(sql, Ticket.class)
                .setParameter("userNo", userNo);
        List<Ticket> ticketInformation = query.getResultList();

        return ticketInformation;
    }

    @Transactional
    public List<Ticket> viewTicketList() {
        String sql = "SELECT t FROM Ticket t";
        TypedQuery<Ticket> query = em.createQuery(sql, Ticket.class);
        List<Ticket> ticketList = query.getResultList();
        return ticketList;
    }

    @Transactional
    public void ticketBookService(Long movieNo, Long locationNo, Long userNo, LocalDate date, List<List<Integer>> selectedSeats, int totalPrice) {

        Movies movies = movieService.getOneMovie(movieNo);
        Location location = locationService.findOneLocation(locationNo);
        User user = userService.getOneUser(userNo);
        Ticket ticket = ticketService.createTicket(date, totalPrice, movies, location, user);

        attachSeatsToTicket(selectedSeats, ticket);
        em.persist(ticket);

    }
    private void attachSeatsToTicket(List<List<Integer>> selectedSeats, Ticket ticket) {
        List<SeatCoordinates> seatCoordinatesList = arrangeSeats(selectedSeats);
        for (SeatCoordinates seatCoordinates : seatCoordinatesList) {
            Seat seat = new Seat();
            seat.setTicket(ticket);
            seat.setSeatRowNo(seatCoordinates.getRow());
            seat.setSeatColumnNo(seatCoordinates.getColumn());
            ticket.getSeatList().add(seat);
        }
    }

    public List<SeatCoordinates> arrangeSeats(List<List<Integer>> selectedSeats) {
        // SeatCoordinates 객체를 저장할 List 생성
        List<SeatCoordinates> seatList = new ArrayList<>();

        // selectedSeats를 순회하면서 SeatCoordinates 객체 생성 후 List에 추가
        for (List<Integer> seat : selectedSeats) {
            // 각 좌석의 행과 열을 가져옴
            int row = seat.get(0);
            int column = seat.get(1);

            // SeatCoordinates 객체 생성
            SeatCoordinates seatCoordinates = new SeatCoordinates(row, column);

            // List에 SeatCoordinates 객체 추가
            seatList.add(seatCoordinates);
        }
        return seatList;
    }



}
