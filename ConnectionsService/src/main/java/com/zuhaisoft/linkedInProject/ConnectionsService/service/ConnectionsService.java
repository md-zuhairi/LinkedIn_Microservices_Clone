package com.zuhaisoft.linkedInProject.ConnectionsService.service;

import com.zuhaisoft.linkedInProject.ConnectionsService.auth.AuthContextHolder;
import com.zuhaisoft.linkedInProject.ConnectionsService.entity.Person;
import com.zuhaisoft.linkedInProject.ConnectionsService.event.UserConnectionRequestEvent;
import com.zuhaisoft.linkedInProject.ConnectionsService.exception.BadRequestException;
import com.zuhaisoft.linkedInProject.ConnectionsService.repository.PersonRepository;
import com.zuhaisoft.linkedInProject.ConnectionsService.event.UserConnectionAcceptEvent;
import com.zuhaisoft.linkedInProject.userService.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionsService {

    private final PersonRepository personRepository;
    private final KafkaTemplate<Long, UserConnectionRequestEvent> userConnectionRequestEventKafkaTemplate;
    private final KafkaTemplate<Long, UserConnectionAcceptEvent> userConnectionAcceptEventKafkaTemplate;

    public List<Person> getFirstDegreeConnectionsOfUser(Long userId){
        log.info("Getting first degree connections of user with ID: {}", userId);

        return personRepository.getFirstDegreeConnections(userId);
    }

    public List<Person> getSecondDegreeConnectionsOfUser(Long userId) {
        log.info("Getting second degree connections of user with ID: {}", userId);

        return  personRepository.getSecondDegreeConnections(userId);
    }

    public List<Person> getThirdDegreeConnectionsOfUser(Long userId) {
        log.info("Getting third degree connections of user with ID: {}", userId);

        return  personRepository.getThirdDegreeConnections(userId);
    }

    public void sendConnectionRequest(Long receiverId){
        Long senderId = AuthContextHolder.getCurrentUserId();
        log.info("sending connection request with senderId: {}, receiverId: {}", senderId, receiverId);

        if (senderId.equals(receiverId)) {
            throw new BadRequestException("Both sender and receiver are the same");
        }

        boolean alreadySentRequest = personRepository.connectionRequestExists(senderId, receiverId);
        if (alreadySentRequest) {
            throw new BadRequestException("Connection request already exists, cannot send again");
        }

        boolean alreadyConnected = personRepository.alreadyConnected(senderId, receiverId);
        if (alreadyConnected) {
            throw new BadRequestException("Already connected users, cannot add connection request");
        }

        personRepository.addConnectionRequest(senderId, receiverId);

        UserConnectionRequestEvent connectionRequestEvent = UserConnectionRequestEvent.builder()
                .name(personRepository.findNameByUserId(senderId))
                .receiverId(receiverId)
                .senderId(senderId)
                .build();

        userConnectionRequestEventKafkaTemplate.send("connection_request_topic", connectionRequestEvent);
        log.info("Successfully sent the connection request to the user ID: " + receiverId);

    }

    public void acceptConnectionRequest(Long senderId){
        Long receiverId = AuthContextHolder.getCurrentUserId();
        log.info("Accepting a connection request with senderId: {}, receiverId: {}", senderId, receiverId);

        if (senderId.equals(receiverId)) {
            throw new BadRequestException("Both sender and receiver are the same");
        }

        boolean alreadyConnected = personRepository.alreadyConnected(senderId, receiverId);
        if (alreadyConnected) {
            throw new BadRequestException("Already connected users, cannot accept connection request again");
        }

        boolean alreadySentRequest = personRepository.connectionRequestExists(senderId, receiverId);
        if (!alreadySentRequest) {
            throw new BadRequestException("No Connection request exists, cannot accept without request");
        }

        personRepository.acceptConnectionRequest(senderId, receiverId);

        UserConnectionAcceptEvent connectionAcceptEvent = UserConnectionAcceptEvent.builder()
                .name(personRepository.findNameByUserId(senderId))
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        userConnectionAcceptEventKafkaTemplate.send("connection_accept_topic", connectionAcceptEvent);

        log.info("Successfully accepted the connection request from the senderId: {}, receiverId: {}", senderId,
                receiverId);
    }

    public void rejectConnectionRequest(Long senderId) {
        Long receiverId = AuthContextHolder.getCurrentUserId();
        log.info("Rejecting a connection request with senderId: {}, receiverId: {}", senderId, receiverId);

        if (senderId.equals(receiverId)) {
            throw new BadRequestException("Both sender and receiver are the same");
        }

        boolean alreadySentRequest = personRepository.connectionRequestExists(senderId, receiverId);
        if (!alreadySentRequest) {
            throw new BadRequestException("No Connection request exists, cannot reject it");
        }

        personRepository.rejectConnectionRequest(senderId, receiverId);

        log.info("Successfully rejected the connection request with senderId: {}, receiverId: {}", senderId,
                receiverId);
    }
}
