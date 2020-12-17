package com.stn.lprmaster.broker.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.common.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class RunnerConfiguration {

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    @Bean
    public ApplicationRunner runner(KafkaAdmin kafkaAdmin) {
        return args -> {
            AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfig());
            DescribeClusterResult describeClusterResult = adminClient.describeCluster();
            List<Node> brokers = new ArrayList<>(describeClusterResult.nodes().get());
            if (!brokers.isEmpty()) {
                for (Node broker : brokers) {
                    log.info("Broker found : [{}:{}]", broker.host(), broker.port());
                }
                registry.getListenerContainer("consumerID").start();
                registry.getListenerContainer("consumerImageID").start();
            }
        };
    }
}
