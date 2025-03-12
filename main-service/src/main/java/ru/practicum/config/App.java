package ru.practicum.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.ewm.client.StatsClient;

@Configuration
public class App {

    @Value("${stats.uri}")
    private String statClientUri;

    @Bean
    public StatsClient statsClient() {

        return new StatsClient(this.statClientUri);
    }
}
