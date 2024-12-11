package com.depth.chart.services.sports;

import com.depth.chart.entities.Position;
import com.depth.chart.entities.Player;
import com.depth.chart.entities.Team;
import com.depth.chart.repository.PositionRepository;
import com.depth.chart.repository.PlayerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PositionService {
    private final PositionRepository positionRepository;
    private final PlayerRepository playerRepository;

    public Position getPosition(String position, Team team) {
        Optional<Position> optionalPlayerPosition = positionRepository.findByPositionAndTeam(position, team);
        return optionalPlayerPosition.orElseGet(Position::new);
    }

    @Transactional
    public void addPosition(Position position, Team team) {
        position.setTeam(team);
        Position newPosition = positionRepository.save(position);

        for (Player player : position.getPlayers()) {
            player.setPosition(newPosition);
            player.setTeam(team);
            playerRepository.save(player);
        }
    }

    @Transactional
    public Position save(Position position) {
        return positionRepository.save(position);
    }
}
