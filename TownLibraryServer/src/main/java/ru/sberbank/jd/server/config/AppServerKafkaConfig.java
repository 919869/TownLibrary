package ru.sberbank.jd.server.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;

@Configuration
public class AppServerKafkaConfig {
    @Bean
    public RecordMessageConverter converter() {
        return new JsonMessageConverter();
    }
    @Bean
    public NewTopic bookcardTopic() {
        return TopicBuilder.name("bookcard.topic")
                .partitions(10)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic subscribeTopic() {
        return TopicBuilder.name("subscribe.topic")
                .partitions(10)
                .replicas(1)
                .build();
    }
}
