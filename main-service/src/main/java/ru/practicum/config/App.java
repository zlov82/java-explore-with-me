package ru.practicum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.ewm.client.StatsClient;

@Configuration
public class App {
    @Bean
    public StatsClient statsClient() {
        //return new StatsClient("http://localhost:9090");  // или инициализация через конструктор
        return new StatsClient("http://stats-server-container:9090");  // или инициализация через конструктор
    }
}
