package com.example.movie.service;

import com.example.movie.dto.SeatDto;
import com.example.movie.dto.TicketDto;
import com.example.movie.entity.*;
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
public class TicketService {
    @Autowired
    EntityManager em;
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<TicketDto> findAll() {
        List<TicketDto> ticketDtoList = ticketRepository.findAll()
                .stream()
                .map(x -> TicketDto.fromTicketEntity(x))
                .toList();
        return ticketDtoList;
    }

    public Ticket createTicket(LocalDate date, int totalPrice, Movies movies, Location location, User user) {
        Ticket ticket = new Ticket();
        ticket.setMovies(movies);
        ticket.setLocation(location);
        ticket.setBookDate(date);
        ticket.setUser(user);
        ticket.setSeatList(new ArrayList<>());
        ticket.setTotalPrice(totalPrice);
        return ticket;
    }

    //티켓 취소 관련 기능들은 bookservice에서 ticketservice로 책임 분리
    @Transactional
    public void ticketCancel(Long ticketNo) {
        // 티켓 조회
        Ticket ticket = em.find(Ticket.class, ticketNo);
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket not found");
        }
        // 환불 처리
        refundTicketPrice(ticket);
        // 티켓 삭제
        em.remove(ticket);
    }

    public void deleteTicketByUser(User user) {
        String ticketQuery = "DELETE FROM Ticket t WHERE t.user = :user";
        em.createQuery(ticketQuery)
                .setParameter("user", user)
                .executeUpdate();
    }

    @Transactional
    private void refundTicketPrice(Ticket ticket) {
        // 사용자 조회
        User user = em.find(User.class, ticket.getUser().getUserNo());
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        // 환불 처리
        int refundAmount = user.getMoney() + ticket.getTotalPrice();
        user.setMoney(refundAmount);

        // 사용자 정보를 업데이트
        em.merge(user);
    }

    @Transactional
    public List<Ticket> viewTicketList() {
        String sql = "SELECT t FROM Ticket t";
        TypedQuery<Ticket> query = em.createQuery(sql, Ticket.class);
        List<Ticket> ticketList = query.getResultList();
        return ticketList;
    }

    @Transactional
    public List<Ticket> viewReservationDetails(Long userNo) {

        String sql = "SELECT t FROM Ticket t WHERE t.user.userNo = :userNo";
        TypedQuery<Ticket> query = em.createQuery(sql, Ticket.class)
                .setParameter("userNo", userNo);
        List<Ticket> ticketInformation = query.getResultList();

        return ticketInformation;
    }

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



}
