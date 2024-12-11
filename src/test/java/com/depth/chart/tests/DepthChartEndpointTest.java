package com.depth.chart.tests;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DepthChartEndpointTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void whenGetFullDepthChartWithNoValuesIsInvoked_thenReturn204() throws Exception {
        mockMvc.perform(get("/api/v1/depth-chart/{leagueId}/get-full-depth-chart", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(2)
    void whenGetBackupsWithNoValuesIsInvoked_thenReturn204() throws Exception {
        mockMvc.perform(post("/api/v1/depth-chart/{leagueId}/{teamId}/get-backups", 1, 1).contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "  \"position\": \"QB\"," +
                                "  \"playerDTO\": {" +
                                "    \"name\": \"Dak Prescott\"," +
                                "    \"number\": \"4\"" +
                                "  }" +
                                "}"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(3)
    void whenRemoveNonExistingPlayersIsInvoked_thenReturn204() throws Exception {
        mockMvc.perform(post("/api/v1/depth-chart/{leagueId}/{teamId}/get-backups", 1, 1).contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "  \"position\": \"QB\"," +
                                "  \"playerDTO\": {" +
                                "    \"name\": \"Dak Prescott\"," +
                                "    \"number\": \"4\"" +
                                "  }" +
                                "}"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(4)
    void whenAddPlayerWithInvalidPositionIsInvoked_thenReturn400() throws Exception {
        mockMvc.perform(put("/api/v1/depth-chart/{leagueId}/{teamId}/add-player", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "  \"position\": \"WR\"," +
                                "  \"playerDTO\": {" +
                                "    \"name\": \"Dak Prescott\"," +
                                "    \"number\": \"4\"" +
                                "  }" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(5)
    void whenAddPlayerWithInvalidNumberIsInvoked_thenReturn400() throws Exception {
        mockMvc.perform(put("/api/v1/depth-chart/{leagueId}/{teamId}/add-player", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "  \"position\": \"QB\"," +
                                "  \"playerDTO\": {" +
                                "    \"name\": \"Dak Prescott\"," +
                                "    \"number\": \"AA\"" +
                                "  }" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(6)
    void whenAddPlayerWithOutOfRangeNumberIsInvoked_thenReturn400() throws Exception {
        mockMvc.perform(put("/api/v1/depth-chart/{leagueId}/{teamId}/add-player", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "  \"position\": \"QB\"," +
                                "  \"playerDTO\": {" +
                                "    \"name\": \"Dak Prescott\"," +
                                "    \"number\": \"100\"" +
                                "  }" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    void whenAddPlayerWithNoPositionDepthIsInvoked_thenReturn201() throws Exception {
        mockMvc.perform(put("/api/v1/depth-chart/{leagueId}/{teamId}/add-player", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "  \"position\": \"QB\"," +
                                "  \"playerDTO\": {" +
                                "    \"name\": \"Dak Prescott\"," +
                                "    \"number\": \"4\"" +
                                "  }" +
                                "}"))
                .andExpect(status().isCreated());
    }

    @Test()
    @Order(8)
    void whenAddPlayerWithSameNumberIsInvoked_thenReturn400() {
        assertThrows(ServletException.class, () ->
            mockMvc.perform(put("/api/v1/depth-chart/{leagueId}/{teamId}/add-player", 1, 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{" +
                                    "  \"position\": \"QB\"," +
                                    "  \"playerDTO\": {" +
                                    "    \"name\": \"Dak Prescott\"," +
                                    "    \"number\": \"4\"" +
                                    "  }" +
                                    "}"))
                    .andExpect(status().isBadRequest())
        );

    }

    @Test
    @Order(9)
    void whenAddPlayerWithValidPositionDepthIsInvoked_thenReturn201() throws Exception {
        mockMvc.perform(put("/api/v1/depth-chart/{leagueId}/{teamId}/add-player", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "  \"position\": \"QB\"," +
                                "  \"playerDTO\": {" +
                                "    \"name\": \"Tom Brady\"," +
                                "    \"number\": \"12\"" +
                                "  }," +
                                "  \"positionDepth\": 1" +
                                "}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(10)
    void whenAddPlayerWithHigherPositionDepthIsInvoked_thenReturn201() throws Exception {
        mockMvc.perform(put("/api/v1/depth-chart/{leagueId}/{teamId}/add-player", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "  \"position\": \"QB\"," +
                                "  \"playerDTO\": {" +
                                "    \"name\": \"Aaron Rodgers\"," +
                                "    \"number\": \"99\"" +
                                "  }," +
                                "  \"positionDepth\": 0" +
                                "}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(11)
    void whenAddPlayerExistingPlayerToADifferentPositionIsInvoked_thenReturn201() throws Exception {
        mockMvc.perform(put("/api/v1/depth-chart/{leagueId}/{teamId}/add-player", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "  \"position\": \"LWR\"," +
                                "  \"playerDTO\": {" +
                                "    \"name\": \"Dak Prescott\"," +
                                "    \"number\": \"4\"" +
                                "  }" +
                                "}"))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(12)
    void whenAddPlayerWithInvalidPositionDepthIsInvoked_thenReturn400() throws Exception {
        mockMvc.perform(put("/api/v1/depth-chart/{leagueId}/{teamId}/add-player", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "  \"position\": \"QB\"," +
                                "  \"playerDTO\": {" +
                                "    \"name\": \"Dak Prescott\"," +
                                "    \"number\": \"4\"" +
                                "  }," +
                                "  \"positionDepth\": 5" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(13)
    void whenRemoveExistingPlayersIsInvoked_thenReturn200() throws Exception {
        mockMvc.perform(post("/api/v1/depth-chart/{leagueId}/{teamId}/get-backups", 1, 1).contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "  \"position\": \"QB\"," +
                                "  \"playerDTO\": {" +
                                "    \"name\": \"Dak Prescott\"," +
                                "    \"number\": \"4\"" +
                                "  }" +
                                "}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(14)
    void whenGetBackupsWithValuesIsInvoked_thenReturn200() throws Exception {
        mockMvc.perform(post("/api/v1/depth-chart/{leagueId}/{teamId}/get-backups", 1, 1).contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "  \"position\": \"QB\"," +
                                "  \"playerDTO\": {" +
                                "    \"name\": \"Aaron Rodgers\"," +
                                "    \"number\": \"99\"" +
                                "  }" +
                                "}"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(15)
    void whenGetBackupsLowestPriorityIsInvoked_thenReturn204() throws Exception {
        mockMvc.perform(post("/api/v1/depth-chart/{leagueId}/{teamId}/get-backups", 1, 1).contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "  \"position\": \"QB\"," +
                                "  \"playerDTO\": {" +
                                "    \"name\": \"Tom Brady\"," +
                                "    \"number\": \"12\"" +
                                "  }" +
                                "}"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(16)
    void whenGetFullDepthChartWithValuesIsInvoked_thenReturn200() throws Exception {
        mockMvc.perform(get("/api/v1/depth-chart/{leagueId}/get-full-depth-chart", 1))
                .andExpect(status().isOk());
    }
}
