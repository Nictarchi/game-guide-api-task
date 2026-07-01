package com.example.game_guide.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RatingRequestDto {
    @NotNull(message = "Оценка обязательна")
    @Min(value = 1, message = "Оценка должна быть от 1 до 10")
    @Max(value = 10, message = "Оценка должна быть от 1 до 10")
    private Integer value;
}
