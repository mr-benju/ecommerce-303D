package dev.rampmaster.ecommerce.users.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI usersOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Users API")
                        .description("Documentacion del microservicio de identidad y gestion de usuarios")
                        .version("v1"))
                .addServersItem(new Server()
                        .url("/users")
                        .description("Context path del microservicio users"));
    }
}

