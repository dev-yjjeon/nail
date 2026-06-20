package com.yurinail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotDto {
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean available;
}
