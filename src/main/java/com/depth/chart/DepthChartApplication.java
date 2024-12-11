package com.depth.chart;

import com.depth.chart.entities.Team;
import com.depth.chart.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@AllArgsConstructor
public class DepthChartApplication {
    private final TeamRepository teamRepository;

    public static void main(String[] args) {
        SpringApplication.run(DepthChartApplication.class, args);
    }

    @Bean
    public CommandLineRunner initialiseTeam() {
        return args -> {
            teamRepository.save(Team.of("Tampa Bay Buccaneers"));
            teamRepository.save(Team.of("New York Jets"));
        };
    }
}