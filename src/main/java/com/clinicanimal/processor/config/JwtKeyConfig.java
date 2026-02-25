package com.clinicanimal.processor.config;

import com.clinicanimal.processor.util.KeyUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPublicKey;

@Configuration
public class JwtKeyConfig {

    @Bean
    public RSAPublicKey publicKey() throws Exception {
        return (RSAPublicKey) KeyUtil.loadPublicKey("public.pem");
    }
}