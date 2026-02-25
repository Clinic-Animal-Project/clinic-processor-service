package com.clinicanimal.processor.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {

            JwtAuthenticationToken auth =
                    (JwtAuthenticationToken) SecurityContextHolder
                            .getContext()
                            .getAuthentication();

            if (auth != null) {
                String token = auth.getToken().getTokenValue();
                System.out.println("Token enviado en el Feign Client: " + token);
                requestTemplate.header("Authorization", "Bearer " + token);
            }else {
                System.out.println("No se encontró JwtAuthenticationToken en el SecurityContext"); // Log adicional
            }
        };
    }

}
