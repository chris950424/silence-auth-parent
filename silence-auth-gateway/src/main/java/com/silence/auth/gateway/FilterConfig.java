package com.silence.auth.gateway;

import com.silence.auth.gateway.filter.SecurityGlobalFilter;
import com.silence.auth.gateway.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Administrator
 */
@Configuration
@ConditionalOnBean(annotation = EnableGatewayAuthStarterConfiguration.class)
public class FilterConfig {


    @Bean
    public SecurityGlobalFilter build() {
        SecurityProperties securityProperties = this.buildS();
        final List<String> noCheckList = securityProperties.getNoCheckList();
        String baseUrl = securityProperties.getBaseUrl();
        return new SecurityGlobalFilter(noCheckList,baseUrl);
    }


    @Bean
    @ConfigurationProperties(prefix = "gateway.secure")
    public SecurityProperties buildS() {
        return new SecurityProperties();
    }
}
