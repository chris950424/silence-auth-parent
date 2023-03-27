package com.silence.auth.client;

import com.silence.auth.client.filter.TokenAuthenticationFilter;
import com.silence.auth.client.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 */
@Configuration
@ConditionalOnBean(annotation = EnableAuthStarterConfiguration.class)
public class FilterConfig {


    @Bean
    public TokenAuthenticationFilter buildT() {
        return new TokenAuthenticationFilter();
    }

    @Bean
    @ConfigurationProperties(prefix = "auth.secure")
    public SecurityProperties buildS() {
        return new SecurityProperties();
    }
}
