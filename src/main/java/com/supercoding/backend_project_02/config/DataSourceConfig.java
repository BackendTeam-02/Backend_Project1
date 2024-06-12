package com.supercoding.backend_project_02.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@Data
public class DataSourceConfig {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
}

