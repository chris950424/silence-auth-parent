package com.silence.auth.gateway;


import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

;


/**
 * ResourceServerConfig
 *
 * @author leo
 * @version 1.1.0
 * @date 2021/12/24
 */
@Configuration
@EnableWebFluxSecurity
@ConditionalOnBean(annotation = EnableGatewayAuthStarterConfiguration.class)
public class ResourceServerConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers("/**").permitAll()
                .anyExchange().authenticated()
                .and().csrf().disable().build();
    }
}

