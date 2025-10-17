package com.config.swagger;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi joinGroupedOpenApi() {
        return GroupedOpenApi
                .builder()
                .group("users")
                .pathsToMatch("/users/**")
                .packagesToScan("com.user.controller")
                .addOpenApiCustomizer(
                        openApi ->
                                openApi
                                        .setInfo(
                                                new Info()
                                                        .title("User API")
                                                        .description("회원가입 및 로그인 API")
                                                        .version("1.0.0")
                                        )
                )
                .build();
    }

}
