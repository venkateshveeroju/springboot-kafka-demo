package com.example.demo;

import com.example.demo.service.SendEmailOnAccountOpeningService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

    private final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    private SendEmailOnAccountOpeningService sendEmailOnAccountOpeningService;

    @KafkaListener(id = "myConsumer2", topics = "topic_0", groupId = "springboot-group-1", autoStartup = "false")
    public void listen(String value,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        logger.info(String.format("\n\n Consumed event from topic %s: key = %-10s value = %s \n\n", topic, key, value));
    }

    @KafkaListener(id = "myConsumer1", topics = "sample_data_orders", groupId = "springboot-group-1", autoStartup = "false")
    public void listen1(String value,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        logger.info(String.format("\n\n Consumed event from topic %s: key = %-10s value = %s \n\n", topic, key, value));
    }


    @KafkaListener(
            id = "myConsumer",
            topics = "topic_0",
            groupId = "springboot-group-1",
            autoStartup = "true"
    )
    public void listenToProjectStatusChange(String record) {
        try {
            logger.info("Raw message: {}", record);

            ObjectMapper objectMapper = new ObjectMapper();


// First, read it as a plain String (unescape JSON)
            String json = objectMapper.readValue(record, String.class);

// Then parse that JSON into EmailDTO
            EmailDTO payload = objectMapper.readValue(json, EmailDTO.class);


            logger.info("Email payload received: {}", payload);

            if (payload == null || payload.getToEmail() == null) {
                logger.warn("Invalid email payload received");
                return;
            }

            sendEmailOnAccountOpeningService.sendSimpleEmail(
                    payload.getToEmail(),
                    payload.getSubject(),
                    payload.getEmailBody()
            );

        } catch (Exception e) {
            logger.error("Error processing Kafka message: {}", record, e);
        }
    }


}
