package com.stn.ester.core.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.util.Map;

@Configuration
@PropertySource("classpath:application-development.properties")
public class DatabaseConfig {
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    public static String extJsonFileConfigPath = "";

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        // read from external JSON file for configuration. Otherwise, it reads the default one from application.properties
        if (!extJsonFileConfigPath.isEmpty()) {
            Map<String, String> dataJson = readJsonFile(extJsonFileConfigPath);
            if (!dataJson.isEmpty()) {
                for (Map.Entry<String, String> entry : dataJson.entrySet()) {
                    switch (entry.getKey()) {
                        case "url":
                            url = entry.getValue();
                            break;
                        case "username":
                            username = entry.getValue();
                            break;
                        case "password":
                            password = entry.getValue();
                            break;
                        default:
                            break;
                    }
                }
            } else {
                System.out.println("Error : can't read JSON file.");
                return null;
            }
        }
        dataSourceBuilder.url(url);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);
        return dataSourceBuilder.build();
    }

    private Map<String, String> readJsonFile(String path) {
        try {
            if (!path.isEmpty()) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> data = objectMapper.readValue(new FileInputStream(path), Map.class);
                return data;
            } else {
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
}
