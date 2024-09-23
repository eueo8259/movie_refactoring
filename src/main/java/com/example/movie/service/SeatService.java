package com.example.movie.service;

import com.example.movie.constant.SeatCoordinates;
import com.example.movie.entity.Seat;
import com.example.movie.entity.Ticket;
import com.example.movie.entity.User;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {
    @Autowired
    EntityManager em;

    public void deleteSeatByUsers(User user) {
        String seatQuery = "DELETE FROM Seat s WHERE s.ticket.user = :user";
        em.createQuery(seatQuery)
                .setParameter("user", user)
                .executeUpdate();
    }


}
