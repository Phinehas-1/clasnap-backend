package com.bigdecimal.clasnapp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
  info = @Info(
    contact = @Contact(
      name = "Phinehas Enakireru",
      email = "phinehasenakireru@gmail.com"
    ),
    title = "clasnapp backend api documentation",
    version = "1.0"
  ),
  servers = {
    @Server(description = "Local ENV", url = "http://localhost:8080"),
  }
)
@SecurityScheme(
  name = "bearerAuth",
  scheme = "bearer",
  type = SecuritySchemeType.HTTP,
  bearerFormat = "JWT",
  in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {}
