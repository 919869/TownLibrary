package ru.sberbank.jd.client.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableFeignClients(basePackages = "ru.sberbank.jd.client")
public class AppClientConfig {
}
