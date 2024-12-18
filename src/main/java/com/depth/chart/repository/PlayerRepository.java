package com.depth.chart.repository;

import com.depth.chart.entities.Player;
import com.depth.chart.entities.Position;
import com.depth.chart.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByNumberAndPositionAndTeam(String number, Position position, Team team);
}
