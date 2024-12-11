package com.depth.chart.services.depthchart;

import com.depth.chart.dtos.PlayerDTO;
import com.depth.chart.dtos.PlayersDTO;
import com.depth.chart.dtos.PositionDTO;
import com.depth.chart.entities.Player;
import com.depth.chart.entities.Position;
import com.depth.chart.entities.Team;
import com.depth.chart.services.mappers.ModelMapperService;
import com.depth.chart.services.sports.PlayerService;
import com.depth.chart.services.sports.PositionService;
import com.depth.chart.services.sports.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RetrievalService {
    private final TeamService teamService;
    private final PlayerService playerService;
    private final PositionService positionService;
    private final ModelMapperService modelMapperService;

    public List<PlayerDTO> getBackups(PositionDTO positionDTO, long teamId) {
        Team team = teamService.getTeam(teamId);
        Player player = playerService.getPlayer(positionDTO.getPlayerDTO().getNumber(), team);

        if (player.getNumber() == null || player.getNumber().isEmpty()) {
            return new ArrayList<PlayerDTO>();
        }

        Position position = positionService.getPosition(positionDTO.getPosition(), team);
        List<Player> backups = getBackups(position.getPlayers(), player.getPositionDepth());
        return getBackupDTOs(backups);
    }

    public List<PlayersDTO> getFullDepthChart() {
        var fullDepthChart = new ArrayList<PlayersDTO>();
        List<Team> teams = teamService.getAllTeams();

        for (Team team : teams) {
            List<Position> positions = team.getPositions();
            convertEntitiesToDTOs(fullDepthChart, positions);
        }

        sortByAscendingPositionDepth(fullDepthChart);
        return fullDepthChart;
    }

    private List<Player>getBackups(List<Player> players, int positionDepth) {
        return players.stream().filter(getBackups(positionDepth)).collect(Collectors.toList());
    }

    private Predicate<Player> getBackups(int positionDepth) {
        return player -> player.getPositionDepth() > positionDepth;
    }

    private List<PlayerDTO>getBackupDTOs(List<Player> backups) {
        var backupDTOs = new ArrayList<PlayerDTO>();

        for (Player backup : backups) {
            backupDTOs.add(modelMapperService.convertPlayerEntityToPlayerDTO(backup));
        }

        backupDTOs.sort(Comparator.comparingInt(PlayerDTO::getPositionDepth));
        return backupDTOs;
    }

    private void convertEntitiesToDTOs(List<PlayersDTO>fullDepthChart, List<Position> positions) {
        for (Position position : positions) {
            fullDepthChart.add(modelMapperService.convertPositionEntityToPlayersDTO(position));
        }
    }

    private void sortByAscendingPositionDepth(List<PlayersDTO> fullDepthChart) {
        for (PlayersDTO playersDTO : fullDepthChart) {
            playersDTO.sortPlayersByPositionDepth();
        }
    }
}
