package com.yurinail.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    private Long reservationId;
    private String customerName;
    private String customerPhone;
    private String password;
    private Long menuId;
    private LocalDate resDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
    private String memo;
    private LocalDateTime insDt;
    private LocalDateTime updDt;
}
