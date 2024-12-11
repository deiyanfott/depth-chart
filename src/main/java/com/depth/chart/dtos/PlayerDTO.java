package com.depth.chart.dtos;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PlayerDTO {
    @NotNull(message = "The player name is required.")
    @NotBlank(message = "The player name must be defined.")
    private String name;

    @NotNull(message = "The player number is required.")
    @NotBlank(message = "The player number must be defined.")
    private String number;

    @JsonIgnore
    private int positionDepth;
}
