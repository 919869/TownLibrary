package ru.sberbank.jd.client.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;

@Component
public class AppClientKafkaConfig {
    @Bean
    public NewTopic bookcardTopic() {
        return TopicBuilder.name("bookcard.topic")
                .partitions(10)
                .replicas(1)
                .build();
    }

    @Bean NewTopic subscribeTopic() {
        return TopicBuilder.name("subscribe.topic")
                .partitions(10)
                .replicas(1)
                .build();
    }
}
