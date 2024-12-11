package com.depth.chart.services.depthchart;

import com.depth.chart.dtos.PositionDTO;
import com.depth.chart.entities.Position;
import com.depth.chart.entities.Player;
import com.depth.chart.entities.Team;
import com.depth.chart.services.mappers.ModelMapperService;
import com.depth.chart.services.sports.PlayerService;
import com.depth.chart.services.sports.PositionService;
import com.depth.chart.services.sports.TeamService;
import com.depth.chart.services.validators.ValidatorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Service
@AllArgsConstructor @Slf4j
public class AdditionService {
    private final ValidatorService validatorService;
    private final ModelMapperService modelMapperService;
    private final TeamService teamService;
    private final PositionService positionService;
    private final PlayerService playerService;

    public boolean addPlayerToDepthChart(PositionDTO positionDTO, long teamId) {
        Position positionToBeAdded = modelMapperService.convertDTOToEntity(positionDTO);
        Team team = teamService.getTeam(teamId);
        Position position = positionService.getPosition(positionDTO.getPosition(), team);
        List<Player> players = position.getPlayers();

        if (players == null || players.isEmpty()) {
            log.info("Position does not exist. Creating new position.");
            addNewPosition(positionToBeAdded, team);
            return true;
        }

        int playersListSize = players.size();
        int positionDepth = positionToBeAdded.getPositionDepth();
        log.info("Position Depth is {}", positionDepth);


        if (isPositionDepthInvalid(players, playersListSize, positionDepth)) {
            log.error("The position depth value is invalid.");
            return false;
        }

        int actualPositionDepth = getPositionDepth(positionDepth, playersListSize);
        log.info("Adding entry to the depth chart list with position depth {}.", actualPositionDepth);
        Player player = modelMapperService
                .convertPlayerDTOToPlayerEntity(positionDTO.getPlayerDTO());
        player.setPositionDepth(actualPositionDepth);
        player.setPosition(position);
        addPlayerToList(players, player, actualPositionDepth, teamId);

        return true;
    }

    private void addNewPosition(Position positionToBeAdded, Team team) {
        positionService.addPosition(positionToBeAdded, team);
    }

    private boolean isPositionDepthInvalid(List<Player> players, int playersListSize, int positionDepth) {
        return !players.isEmpty() && playersListSize < positionDepth;
    }

    private int getPositionDepth(int positionDepth, int playersListSize) {
        return positionDepth == -1 || playersListSize == 0 ? playersListSize : positionDepth;
    }

    private void addPlayerToList(List<Player> players, Player player, int positionDepth, long teamId) {
        players.stream()
                .filter(getPlayersWithLowerPosition(positionDepth))
                .forEach(incrementPositionDepth());
        player.setTeam(teamService.getTeam(teamId));
        players.add(player);
        playerService.updatePlayerList(players);
    }

    private Predicate<Player> getPlayersWithLowerPosition(int positionDepth) {
        return player -> player.getPositionDepth() >= positionDepth;
    }

    private Consumer<Player> incrementPositionDepth() {
        return player -> player.setPositionDepth(player.getPositionDepth() + 1);
    }
}
