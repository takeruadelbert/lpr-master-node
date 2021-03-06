package com.stn.lprmaster.broker.kafka.config;

import com.stn.lprmaster.broker.kafka.model.frame.Frame;
import com.stn.lprmaster.broker.kafka.model.image.Image;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfiguration {

    @Value(value = "${kafka.host}")
    private String host;

    @Value(value = "${kafka.port}")
    private Integer port;

    @Value(value = "${kafka.group}")
    private String group;

    private Map<String, Object> setupConsumerConfiguration() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, String.format("%s:%d", host, port));
        config.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return config;
    }

    @Bean
    public ConsumerFactory<String, Frame> consumerFactory() {
        Map<String, Object> config = setupConsumerConfiguration();
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new JsonDeserializer<>(Frame.class));
    }

    @Bean
    public ConsumerFactory<String, Image> consumerFactoryImage() {
        Map<String, Object> config = setupConsumerConfiguration();
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new JsonDeserializer<>(Image.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Frame> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Frame> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Image> kafkaListenerContainerFactoryImage() {
        ConcurrentKafkaListenerContainerFactory<String, Image> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryImage());
        return factory;
    }
}
