package com.depth.chart.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"number", "position_id", "team_id"}))
public class Player {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull @NotBlank
    private String number;

    @NotNull @NotBlank
    private String name;

    @NotNull
    private int positionDepth;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
}
