package com.depth.chart.repository;

import com.depth.chart.entities.Position;
import com.depth.chart.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    Optional<Position> findByPositionAndTeam(String position, Team team);
}
