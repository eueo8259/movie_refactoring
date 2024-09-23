package com.example.movie.service;

import com.example.movie.dto.TotalPriceDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
public class SalesReportService {
    //매출 관련 책임
    @Autowired
    EntityManager em;
    public Long All() {
        String sql = "SELECT SUM(t.totalPrice) FROM Ticket t";
        TypedQuery<Long> query = em.createQuery(sql, Long.class);
        return query.getSingleResult();
    }

    public Long SumMovie() {
        String sql = "SELECT SUM(t.totalPrice) FROM Ticket t WHERE t.movies IN (SELECT t.movies FROM Ticket t)";
        TypedQuery<Long> query = em.createQuery(sql, Long.class);
        return query.getSingleResult();
    }

    public Long SumUser(){
        String sql = "SELECT SUM(t.totalPrice) FROM Ticket t WHERE t.user IN (SELECT t.user FROM Ticket t)";
        TypedQuery<Long> query = em.createQuery(sql, Long.class);
        return query.getSingleResult();
    }

    public Long SumLocation() {
        String sql = "SELECT SUM(t.totalPrice) FROM Ticket t WHERE t.location IN (SELECT t.location FROM Ticket t)";
        TypedQuery<Long> query = em.createQuery(sql, Long.class);
        return query.getSingleResult();
    }

    public Long SumDate() {
        String sql = "SELECT SUM(t.totalPrice) FROM Ticket t WHERE t.bookDate IN (SELECT t.bookDate FROM Ticket t)";
        TypedQuery<Long> query = em.createQuery(sql, Long.class);
        return query.getSingleResult();
    }

    public List<TotalPriceDto> findMovieTotalPrice() {
        String sql = "SELECT t.movies.movieTitle, SUM(t.totalPrice)  FROM Ticket t GROUP BY t.movies.movieTitle ";
        TypedQuery<TotalPriceDto> query = em.createQuery(sql, TotalPriceDto.class);
        return query.getResultList();
    }

    public List<TotalPriceDto> findUserTotalPrice() {
        String sql = "SELECT t.user.userName, SUM(t.totalPrice) FROM Ticket t GROUP BY t.user.userName ";
        TypedQuery<TotalPriceDto> query = em.createQuery(sql, TotalPriceDto.class);
        return query.getResultList();
    }

    public List<TotalPriceDto> findLocationTotalPrice() {
        String sql = "SELECT t.location.locationName, SUM(t.totalPrice) FROM Ticket t GROUP BY t.location.locationName";
        TypedQuery<TotalPriceDto> query = em.createQuery(sql, TotalPriceDto.class);
        return query.getResultList();
    }


    public List<TotalPriceDto> findDateTotalPrice() {
        String sql = "SELECT t.bookDate, SUM(t.totalPrice)  FROM Ticket t GROUP BY t.bookDate ";
        TypedQuery<TotalPriceDto> query = em.createQuery(sql, TotalPriceDto.class);
        return query.getResultList();
    }

    // 매출 합계를 한국 원화 형식으로 포맷팅합니다. 단위 테스트 완료
    public String formattedSum(Long sum){
        Locale locale = new Locale("ko", "KR");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        return currencyFormatter.format(sum);
    }

}
