package com.depth.chart;

import com.depth.chart.entities.Team;
import com.depth.chart.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DepthChartApplication {
    @Autowired
    private TeamRepository teamRepository;

    public static void main(String[] args) {
        SpringApplication.run(DepthChartApplication.class, args);
    }

    @Bean
    public CommandLineRunner initialiseTeam() {
        return (args) -> {
            teamRepository.save(Team.of("Tampa Bay Buccaneers"));
            teamRepository.save(Team.of("New York Jets"));
        };
    }
}