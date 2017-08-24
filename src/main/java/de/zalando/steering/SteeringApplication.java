package de.zalando.steering;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import org.zalando.fahrschein.AccessTokenProvider;

import org.zalando.fashionmovers.fahrschein.EnableFahrschein;
import org.zalando.fashionmovers.fahrschein.NakadiFahrscheinProperties;

import org.zalando.stups.tokens.AccessTokens;

import org.zalando.twintip.spring.SchemaResource;

@SpringBootApplication
@EnableConfigurationProperties(SteeringProperties.class)
@Import({ SchemaResource.class })
@EnableFahrschein
public class SteeringApplication {

    public static void main(final String[] args) {
        SpringApplication.run(SteeringApplication.class, args);
    }

    @Bean
    public AccessTokenProvider accessTokenProvider(@Autowired final AccessTokens accessTokens,
            @Autowired final NakadiFahrscheinProperties nakadiFahrscheinProperties) {
        return () -> accessTokens.get(nakadiFahrscheinProperties.getToken());
    }
}
