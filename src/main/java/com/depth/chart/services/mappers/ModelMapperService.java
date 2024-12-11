package com.depth.chart.services.mappers;

import com.depth.chart.dtos.PlayerDTO;
import com.depth.chart.dtos.PlayersDTO;
import com.depth.chart.entities.Position;
import com.depth.chart.dtos.PositionDTO;
import com.depth.chart.entities.Player;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ModelMapperService {
    private final ModelMapper modelMapper;

    public Position convertDTOToEntity(PositionDTO dto) {
        Player player = convertPlayerDTOToPlayerEntity(dto.getPlayerDTO());
        Position position = convertPositionDTOToPositionEntity(dto);
        position.setPlayers(List.of(player));

        return position;
    }

    public PlayersDTO convertPositionEntityToPlayersDTO(Position position) {
        return modelMapper.map(position, PlayersDTO.class);
    }

    public Position convertPositionDTOToPositionEntity(PositionDTO dto) {
        return modelMapper.map(dto, Position.class);
    }

    public Player convertPlayerDTOToPlayerEntity(PlayerDTO dto) {
        return modelMapper.map(dto, Player.class);
    }

    public PositionDTO convertPositionEntityToPositionDTO(Position entity) {
        return modelMapper.map(entity, PositionDTO.class);
    }

    public PlayerDTO convertPlayerEntityToPlayerDTO(Player entity) {
        return modelMapper.map(entity, PlayerDTO.class);
    }
}
