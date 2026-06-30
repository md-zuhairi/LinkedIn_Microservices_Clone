package com.zuhaisoft.linkedInProject.notification_service.consumer;

import com.zuhaisoft.linkedInProject.connectionsService.event.UserConnectionAcceptEvent;
import com.zuhaisoft.linkedInProject.connectionsService.event.UserConnectionRequestEvent;
import com.zuhaisoft.linkedInProject.notification_service.entity.Notification;
import com.zuhaisoft.linkedInProject.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionsConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "connection_request_topic")
    public void handleConnectionRequest(UserConnectionRequestEvent connectionRequestEvent){
        log.info("Received notification: {}", connectionRequestEvent);
        String message = String.format("The user %s has sent you an connection request", connectionRequestEvent.getName());

        Notification notification = Notification.builder()
                .userId(connectionRequestEvent.getReceiverId())
                .message(message)
                .build();

        notificationService.addNotification(notification);
    }

    @KafkaListener(topics = "connection_accept_topic")
    public void handleConnectionAccept(UserConnectionAcceptEvent connectionAcceptEvent){
        log.info("Received notification: {}", connectionAcceptEvent);
        String message = String.format("The user %s has accepted you an connection request", connectionAcceptEvent.getName());

        Notification notification = Notification.builder()
                .userId(connectionAcceptEvent.getSenderId())
                .message(message)
                .build();

        notificationService.addNotification(notification);
    }


}
