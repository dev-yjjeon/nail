package com.yurinail.mapper;

import com.yurinail.domain.Reservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ReservationMapper {
    List<Reservation> selectReservationsByDate(LocalDate resDate);
    Reservation selectReservationById(Long reservationId);
    void insertReservation(Reservation reservation);
    void updateReservation(Reservation reservation);
    void deleteReservation(Long reservationId);
    List<Reservation> selectReservationsByCustomer(@Param("customerName") String customerName, @Param("customerPhone") String customerPhone, @Param("password") String password);
}
