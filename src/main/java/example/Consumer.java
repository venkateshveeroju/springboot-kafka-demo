package example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.service.SendEmailOnAccountOpeningService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.mail.MailException;
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


    @KafkaListener(id = "myConsumer", topics = "topic_0", groupId = "springboot-group-1", autoStartup = "false")
    public void listenToProjectStatusChange(String record) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        if (record.startsWith("{")) {
            EmailDTO payload = objectMapper.readValue(record, EmailDTO.class);
            logger.info("Email payload received: {}", payload);
            if (payload == null || payload.getToEmail() == null) {
                logger.warn("Invalid email payload received");
                return;
            }
            try {
                sendEmailOnAccountOpeningService.sendSimpleEmail(
                        payload.getToEmail(),
                        payload.getSubject(),
                        payload.getEmailBody()
                );
            } catch (MailException e) {
                logger.error("Could not send e-mail", e);
            }
            // proceed with email sending
        } else {
            logger.warn("Received non-JSON message: {}", record.toString());
        }





    }
}