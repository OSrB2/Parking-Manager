package io.github.spring.api_parking_manager.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
  info = @Info(
    title = "Parking Manager API",
    version = "Version 1.0.0",
    contact = @Contact(
      name = "Pedro Oliveira",
      email = "pedro_oliva@outlook.com.br"
    )
  )
)
public class OpenApiConfiguration {
  
}
