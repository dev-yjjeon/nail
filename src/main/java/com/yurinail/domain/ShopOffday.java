package com.yurinail.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopOffday {
    private Long offdayId;
    private LocalDate offDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String offReason;
}
