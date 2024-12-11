package com.depth.chart.services.depthchart;

import com.depth.chart.dtos.PlayerDTO;
import com.depth.chart.dtos.PositionDTO;
import com.depth.chart.entities.Player;
import com.depth.chart.entities.Position;
import com.depth.chart.entities.Team;
import com.depth.chart.services.mappers.ModelMapperService;
import com.depth.chart.services.sports.PlayerService;
import com.depth.chart.services.sports.PositionService;
import com.depth.chart.services.sports.TeamService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Service
@AllArgsConstructor @Slf4j
public class RemovalService {
    private final ModelMapperService modelMapperService;
    private final TeamService teamService;
    private final PositionService positionService;
    private final PlayerService playerService;

    public PlayerDTO removePlayerFromDepthChart(PositionDTO positionDTO, long teamId) {
        Team team = teamService.getTeam(teamId);
        String positionCode = positionDTO.getPosition();
        Position position = positionService.getPosition(positionCode, team);
        List<Player> players = position.getPlayers();

        if (players == null || players.isEmpty()) {
            log.info("No players are found in team id {} for the {} position.", teamId, positionCode);
            return new PlayerDTO();
        }

        Player playerToBeRemoved = getPlayerToBeRemoved(positionDTO, players);
        String number = playerToBeRemoved.getNumber();

        if (number == null || number.isEmpty()) {
            log.info("The player requested to be removed was not found.");
            return new PlayerDTO();
        }

        List<Player> playersAfterRemoval = removePlayer(position, players, playerToBeRemoved);
        updatePlayerPositionDepth(playersAfterRemoval, playerToBeRemoved.getPositionDepth());
        return modelMapperService.convertPlayerEntityToPlayerDTO(playerToBeRemoved);
    }

    private Player getPlayerToBeRemoved(PositionDTO positionDTO, List<Player> players) {
        return players.stream().filter(getPlayer(positionDTO)).findFirst().orElse(new Player());
    }

    private Predicate<Player> getPlayer(PositionDTO positionDTO) {
        return player -> player.getNumber().equals(positionDTO.getPlayerDTO().getNumber());
    }

    private List<Player> removePlayer(Position position, List<Player> players, Player playerToBeRemoved) {
        players.remove(playerToBeRemoved);
        return positionService.save(position).getPlayers();
    }

    private void updatePlayerPositionDepth(List<Player> players, int positionDepth) {
        players.stream()
                .filter(getPlayersWithLowerPosition(positionDepth))
                .forEach(decrementPositionDepth());
        playerService.updatePlayerList(players);
    }

    private Predicate<Player> getPlayersWithLowerPosition(int positionDepth) {
        return player -> player.getPositionDepth() > positionDepth;
    }

    private Consumer<Player> decrementPositionDepth() {
        return player -> player.setPositionDepth(player.getPositionDepth() - 1);
    }
}
