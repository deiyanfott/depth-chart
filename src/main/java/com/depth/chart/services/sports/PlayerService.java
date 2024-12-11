package com.depth.chart.services.sports;

import com.depth.chart.entities.Player;
import com.depth.chart.entities.Position;
import com.depth.chart.entities.Team;
import com.depth.chart.repository.PlayerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;

    @Transactional
    public void updatePlayerList(List<Player> players) {
        playerRepository.saveAll(players);
    }

    public Player getPlayer(String number, Position position, Team team) {
        Optional<Player> optionalPlayer = playerRepository
                .findByNumberAndPositionAndTeam(number, position, team);
        return optionalPlayer.orElseGet(Player::new);
    }
}
