package com.yurinail.service;

import com.yurinail.domain.NailMenu;
import com.yurinail.domain.Reservation;
import com.yurinail.domain.ShopOffday;
import com.yurinail.dto.SlotDto;
import com.yurinail.mapper.MenuMapper;
import com.yurinail.mapper.OffdayMapper;
import com.yurinail.mapper.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationMapper reservationMapper;
    private final MenuMapper menuMapper;
    private final OffdayMapper offdayMapper;

    public List<SlotDto> getAvailableSlots(LocalDate date, Long menuId) {
        NailMenu menu = menuMapper.selectMenuById(menuId);
        if (menu == null) {
            throw new IllegalArgumentException("존재하지 않는 메뉴입니다.");
        }

        int duration = menu.getDurationMinutes();
        List<SlotDto> slots = new ArrayList<>();

        // 10:00부터 19:30까지 30분 단위로 슬롯 생성 (영업시간은 10:00 ~ 20:00)
        LocalTime workStart = LocalTime.of(10, 0);
        LocalTime workEnd = LocalTime.of(20, 0);

        // 해당 날짜의 기존 예약 조회 (PENDING, CONFIRMED 상태인 예약들만 겹침 확인 대상)
        List<Reservation> existingReservations = reservationMapper.selectReservationsByDate(date);
        // 해당 날짜의 휴무일 정보 조회
        List<ShopOffday> offdays = offdayMapper.selectOffdaysByDate(date);

        LocalTime time = workStart;
        while (time.isBefore(workEnd)) {
            LocalTime slotStart = time;
            LocalTime slotEnd = time.plusMinutes(duration);
            boolean available = true;

            // 1. 종료 시간이 영업 시간을 초과하면 예약 불가
            if (slotEnd.isAfter(workEnd)) {
                available = false;
            }

            // 2. 휴무일 범위와 겹치는지 체크
            if (available) {
                for (ShopOffday offday : offdays) {
                    if (offday.getStartTime() == null || offday.getEndTime() == null) {
                        // 해당 일 전체 휴무
                        available = false;
                        break;
                    } else {
                        // 부분 휴무 범위와 겹치는지 확인
                        if (isOverlapping(slotStart, slotEnd, offday.getStartTime(), offday.getEndTime())) {
                            available = false;
                            break;
                        }
                    }
                }
            }

            // 3. 기존 예약과 겹치는지 체크
            if (available) {
                for (Reservation res : existingReservations) {
                    if ("PENDING".equals(res.getStatus()) || "CONFIRMED".equals(res.getStatus())) {
                        if (isOverlapping(slotStart, slotEnd, res.getStartTime(), res.getEndTime())) {
                            available = false;
                            break;
                        }
                    }
                }
            }

            slots.add(new SlotDto(slotStart, slotEnd, available));
            time = time.plusMinutes(30);
        }

        return slots;
    }

    public void createReservation(Reservation reservation) {
        // 예약 정보 정합성 검증
        NailMenu menu = menuMapper.selectMenuById(reservation.getMenuId());
        if (menu == null) {
            throw new IllegalArgumentException("존재하지 않는 메뉴입니다.");
        }

        // 서비스 레이어에서 종료 시간 계산
        LocalTime startTime = reservation.getStartTime();
        LocalTime endTime = startTime.plusMinutes(menu.getDurationMinutes());
        reservation.setEndTime(endTime);

        LocalTime workEnd = LocalTime.of(20, 0);
        if (endTime.isAfter(workEnd)) {
            throw new IllegalStateException("영업 종료 시간(20:00)을 초과하는 예약은 불가합니다.");
        }

        LocalDate date = reservation.getResDate();

        // 2차 검증: 휴무일 및 기존 예약과의 겹침 체크
        List<ShopOffday> offdays = offdayMapper.selectOffdaysByDate(date);
        for (ShopOffday offday : offdays) {
            if (offday.getStartTime() == null || offday.getEndTime() == null) {
                throw new IllegalStateException("해당 날짜는 휴무일입니다.");
            } else {
                if (isOverlapping(startTime, endTime, offday.getStartTime(), offday.getEndTime())) {
                    throw new IllegalStateException("선택하신 시간대는 휴무 시간과 겹칩니다.");
                }
            }
        }

        List<Reservation> existingReservations = reservationMapper.selectReservationsByDate(date);
        for (Reservation res : existingReservations) {
            if ("PENDING".equals(res.getStatus()) || "CONFIRMED".equals(res.getStatus())) {
                if (isOverlapping(startTime, endTime, res.getStartTime(), res.getEndTime())) {
                    throw new IllegalStateException("선택하신 시간대에 이미 다른 예약이 존재합니다.");
                }
            }
        }

        // 값 초기화 및 삽입
        reservation.setStatus("PENDING");
        reservation.setInsDt(LocalDateTime.now());
        reservation.setUpdDt(LocalDateTime.now());

        reservationMapper.insertReservation(reservation);
    }

    public List<Reservation> getReservations(String name, String phone, String password) {
        return reservationMapper.selectReservationsByCustomer(name, phone, password);
    }

    public List<Reservation> getReservationsByDate(LocalDate date) {
        return reservationMapper.selectReservationsByDate(date);
    }

    public void approveReservation(Long reservationId) {
        Reservation res = reservationMapper.selectReservationById(reservationId);
        if (res != null) {
            res.setStatus("CONFIRMED");
            res.setUpdDt(LocalDateTime.now());
            reservationMapper.updateReservation(res);
        } else {
            throw new IllegalArgumentException("존재하지 않는 예약입니다.");
        }
    }

    public void cancelReservation(Long reservationId) {
        Reservation res = reservationMapper.selectReservationById(reservationId);
        if (res != null) {
            res.setStatus("CANCELLED");
            res.setUpdDt(LocalDateTime.now());
            reservationMapper.updateReservation(res);
        } else {
            throw new IllegalArgumentException("존재하지 않는 예약입니다.");
        }
    }

    private boolean isOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }
}
