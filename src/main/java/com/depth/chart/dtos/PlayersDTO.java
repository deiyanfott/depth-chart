package com.depth.chart.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class PlayersDTO {
    private String position;
    private List<PlayerDTO> players;

    public void sortPlayersByPositionDepth() {
        players.sort(Comparator.comparingInt(PlayerDTO::getPositionDepth));
    }
}
