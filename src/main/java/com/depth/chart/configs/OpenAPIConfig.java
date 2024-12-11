package com.depth.chart.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI localOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Depth Chart")
                        .version("1.0")
                        .description("This is a for the Fan Duel requirement"))
                .addServersItem(new Server().url("http://localhost:8080"));
    }
}
