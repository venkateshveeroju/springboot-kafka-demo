package example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import example.EmailDTO;
import example.Producer;
import example.service.SendEmailOnAccountOpeningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {
private static final Logger logger = LoggerFactory.getLogger(EmailController.class);
    @Autowired
    private SendEmailOnAccountOpeningService sendEmailOnAccountOpeningService;
    @Autowired
    Producer kafkaProducer;
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
    @GetMapping("/test")
    public String test() {

        return "Test api!";
    }
}
