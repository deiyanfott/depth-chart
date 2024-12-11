package com.depth.chart.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PositionDTO {
    @NotNull(message = "The player position is required.")
    @NotBlank(message = "The player position must be defined.")
    private String position;

    @NotNull(message = "The player profile is required.")
    private PlayerDTO playerDTO;

    private int positionDepth = -1;
}
