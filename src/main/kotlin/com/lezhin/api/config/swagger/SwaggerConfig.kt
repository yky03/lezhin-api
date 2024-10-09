package com.lezhin.api.config.swagger

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.Components
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        val securityScheme = SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .name("Authorization")
            .`in`(SecurityScheme.In.HEADER)

        return OpenAPI()
            .info(
                Info().title("Lezhin API")
                    .description("Lezhin API documentation")
                    .version("v1.0")
            )
            .addSecurityItem(SecurityRequirement().addList("Authorization"))
            .components(
                Components()
                    .addSecuritySchemes("Authorization", securityScheme)
            )
    }
}