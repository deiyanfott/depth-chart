package com.depth.chart.services.sports;

import com.depth.chart.entities.Team;
import com.depth.chart.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;

    public Team getTeam(Long id) {
        return teamRepository.findById(id).orElseGet(Team::new);
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }
}
