package example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import org.springframework.kafka.support.SendResult;


import java.util.concurrent.CompletableFuture;

@Service
public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    private static final String TOPIC = "topic_0";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String key, String value) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC, key, value);
    }
    public void sendEmailMessage(EmailDTO emailDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String strEmailDTO =  objectMapper.writeValueAsString(emailDTO);
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC,emailDTO.getToEmail(),strEmailDTO);

    }
}