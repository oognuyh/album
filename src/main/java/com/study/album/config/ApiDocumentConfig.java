package com.study.album.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class ApiDocumentConfig {

  @Autowired private ServerProperties serverProperties;

  @Bean
  public OpenAPI initialize() {
    return new OpenAPI()
        .components(new Components()
          .addSecuritySchemes("jwt", new SecurityScheme()
            .name("jwt")
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)))
        .servers(List.of(new Server()
          .url("https://oognuyh.asuscomm.com/api")
          .description("external access"), new Server()
          .url(String.format("http://localhost:%d", serverProperties.getPort()))
          .description("internal access")))
        .addSecurityItem(new SecurityRequirement().addList("jwt"))
        .info(new Info()
          .title("Album API Document")
          .license(new License()
            .name("MIT License")
            .url("http://opensource.org/licenses/MIT"))
          .contact(new Contact()
            .email("oognuyh@gmail.com")
            .name("oognuyh")));
  }
}
