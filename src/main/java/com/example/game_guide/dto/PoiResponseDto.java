package com.example.game_guide.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PoiResponseDto {
    private Long id;
    private String name;
    private String categoryName;
    private Double coordX;
    private Double coordY;
    private Double avgRating;
    private Double distance;
}
