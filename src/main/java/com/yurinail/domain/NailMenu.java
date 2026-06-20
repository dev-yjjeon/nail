package com.yurinail.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NailMenu {
    private Long menuId;
    private String menuName;
    private Integer price;
    private Integer durationMinutes;
    private String description;
    private String useYn;
}
