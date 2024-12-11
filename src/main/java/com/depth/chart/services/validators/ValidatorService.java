package com.depth.chart.services.validators;

import com.depth.chart.dtos.PositionDTO;
import com.depth.chart.enums.PositionCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
public class ValidatorService {
    public boolean hasPassedValidations(PositionDTO dto) {
        boolean isPlayerNumberValid = checkPlayerNumber(dto.getPlayerDTO().getNumber());
        boolean isPositionValid = checkPosition(dto.getPosition());
        boolean isPositionDepthValid = checkPositionDepth(dto.getPositionDepth());
        return isPlayerNumberValid && isPositionValid && isPositionDepthValid;
    }

    public boolean checkPlayerNumber(String playerNumber) {
        try {
            int number = Integer.parseInt(playerNumber);

            if (number < 0 || number > 99) {
                log.error("The number should be between 00 to 99.");
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            log.error("The value is not a valid number.");
            return false;
        }
    }

    public boolean checkPosition(String position) {
        return Arrays.stream(PositionCodes.values())
                .anyMatch(code -> code.toString().equalsIgnoreCase(position));
    }

    public boolean checkPositionDepth(int positionDepth) {
        return positionDepth > -2;
    }
}
