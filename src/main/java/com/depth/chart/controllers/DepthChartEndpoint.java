package com.depth.chart.controllers;

import com.depth.chart.dtos.PlayerDTO;
import com.depth.chart.dtos.PlayersDTO;
import com.depth.chart.services.depthchart.AdditionService;
import com.depth.chart.dtos.PositionDTO;
import com.depth.chart.services.depthchart.RemovalService;
import com.depth.chart.services.depthchart.RetrievalService;
import com.depth.chart.services.validators.ValidatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/depth-chart/")
@AllArgsConstructor
public class DepthChartEndpoint {
    private final ValidatorService validatorService;
    private final AdditionService additionService;
    private final RemovalService removalService;
    private final RetrievalService retrievalService;

    @PutMapping("{leagueId}/{teamId}/add-player")
    @Operation(summary = "Adding player to the depth chart",
            description = "This will add a player to the depth chart after all validation checks have been satisfied")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Adding of player is successful"),
            @ApiResponse(responseCode = "400", description = "Request body has invalid values")
    })
    public ResponseEntity<?> addPlayerToDepthChart(@PathVariable @NotNull long leagueId,
                                                   @PathVariable @NotNull long teamId,
                                                   @RequestBody @Valid PositionDTO positionDTO) {
        boolean isValid = validatorService.hasPassedValidations(positionDTO);

        if (isValid) {
            boolean isSuccessfullyAdded = additionService.addPlayerToDepthChart(positionDTO, teamId);

            if (isSuccessfullyAdded) {
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{leagueId}/{teamId}/remove-player")
    @Operation(summary = "Removing a player from the depth chart",
            description = "This will remove a player from the depth chart and return the removed player or an empty list if no one was removed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Removal of player is successful"),
            @ApiResponse(responseCode = "204", description = "Player was not found"),
            @ApiResponse(responseCode = "400", description = "Request body has invalid values")
    })
    public ResponseEntity<?> removePlayerFromDepthChart(@PathVariable @NotNull long leagueId,
                                                        @PathVariable @NotNull long teamId,
                                                        @RequestBody @Valid PositionDTO positionDTO) {
        boolean isValid = validatorService.hasPassedValidations(positionDTO);

        if (isValid) {
            PlayerDTO removedPlayerDTO = removalService.removePlayerFromDepthChart(positionDTO, teamId);
            String number = removedPlayerDTO.getNumber();

            if (number == null || number.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(removedPlayerDTO, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("{leagueId}/{teamId}/get-backups")
    @Operation(summary = "Get the player's backup from the depth chart",
            description = "This will return the backup of the chosen player or an empty list if there are no back ups")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieval of backups is successful"),
            @ApiResponse(responseCode = "204", description = "No backups found"),
            @ApiResponse(responseCode = "400", description = "Request body has invalid values")
    })
    public ResponseEntity<?> getBackups(@PathVariable @NotNull long leagueId,
                                        @PathVariable @NotNull long teamId,
                                        @RequestBody @Valid PositionDTO positionDTO) {
        boolean isValid = validatorService.hasPassedValidations(positionDTO);

        if (isValid) {
            List<PlayerDTO> backups = retrievalService.getBackups(positionDTO, teamId);

            if (backups.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(backups, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{leagueId}/get-full-depth-chart")
    @Operation(summary = "Get the depth chart",
            description = "This will return the full depth chart list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieval of depth chart is successful"),
            @ApiResponse(responseCode = "204", description = "Depth chart is empty")
    })
    public ResponseEntity<?> getFullDepthChart(@PathVariable @NotNull long leagueId) {
        List<PlayersDTO> fullDepthChart = retrievalService.getFullDepthChart();

        if (fullDepthChart.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(fullDepthChart, HttpStatus.OK);
        }
    }
}
