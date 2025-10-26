// src/main/java/com/gestao/usuarios/config/OpenApiConfig.java
package com.gestao.usuarios.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gestão de Usuários API")
                        .version("v1")
                        .description("Documentação automática gerada pelo springdoc-openapi.")
                        .contact(new Contact().name("BRITech").email("suporte@britech.com"))
                        .license(new License().name("MIT")))
                .components(new Components().addSecuritySchemes(
                        SECURITY_SCHEME_NAME,
                        new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                ))
                // adiciona o botão Authorize global (opcional; remove se preferir não global)
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }
}
