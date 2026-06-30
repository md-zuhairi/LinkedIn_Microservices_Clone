package com.zuhaisoft.linkedInProject.ConnectionsService.consumer;

import com.zuhaisoft.linkedInProject.ConnectionsService.service.PersonService;
import com.zuhaisoft.linkedInProject.userService.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceConsumer {

    private final PersonService personService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "user_created_topic")
    public void handlePersonCreated(UserCreatedEvent userCreatedEvent){
        try {
            log.info("Received Notification: Person created with id: {}", userCreatedEvent.getUserId());
            personService.createPerson(userCreatedEvent.getUserId(), userCreatedEvent.getName());
        } catch (Exception e) {
            log.error("Failed to parse Kafka message: {}", e.getMessage());
        }
    }

}
