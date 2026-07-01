package com.example.game_guide.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GuideRequestDto {
    @NotBlank(message = "Текст гайда не может быть пустым")
    private String content;
}
