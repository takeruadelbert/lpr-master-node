package com.stn;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.common.Node;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication(exclude = RepositoryRestMvcAutoConfiguration.class)
@EnableRabbit
@EnableAsync
@Slf4j
public class RestApplication extends SpringBootServletInitializer {

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(RestApplication.class);
    }

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
            }
        };
    }
}
