package com.example.demo.controller;

import com.example.demo.EmailDTO;
import com.example.demo.Producer;
import com.example.demo.service.SendEmailOnAccountOpeningService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class EmailController {
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);
    @Autowired
    private SendEmailOnAccountOpeningService sendEmailOnAccountOpeningService;
    @Autowired
    Producer kafkaProducer;
    @GetMapping("/test")
    public String  test() {
        return "test";
    }
    @PostMapping("/sendemail")
    public void sendProjectStatusEmail(@RequestBody EmailDTO emailDTO) throws JsonProcessingException {
        logger.info("Sending mailing request: " + emailDTO.toString());
        kafkaProducer.sendEmailMessage(emailDTO);
    }
    @GetMapping("/send")
    public String sendEmail() {
        sendEmailOnAccountOpeningService.sendSimpleEmail("testveertestveer@gmail.com", "Test Subject", "Hello from Spring Boot!");
        return "Email sent!";
    }
}
