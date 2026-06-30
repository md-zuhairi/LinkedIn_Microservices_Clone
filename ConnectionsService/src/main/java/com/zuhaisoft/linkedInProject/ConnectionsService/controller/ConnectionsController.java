package com.zuhaisoft.linkedInProject.ConnectionsService.controller;

import com.zuhaisoft.linkedInProject.ConnectionsService.entity.Person;
import com.zuhaisoft.linkedInProject.ConnectionsService.service.ConnectionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/core")
public class ConnectionsController {

    private final ConnectionsService connectionsService;

    @GetMapping("/{userId}/first-degree")
    public ResponseEntity<List<Person>> getFirstDegreeConnections(@PathVariable Long userId){
        log.info("Received request to get first degree connections for user with ID: {}", userId);
        List<Person> firstDegreeConnectionsList = connectionsService.getFirstDegreeConnectionsOfUser(userId);
        return new ResponseEntity<>(firstDegreeConnectionsList, HttpStatus.OK);
    }

    @GetMapping("/{userId}/second-degree")
    public ResponseEntity<List<Person>> getSecondDegreeConnections(@PathVariable Long userId){
        log.info("Received request to get second degree connections for user with ID: {}", userId);
        List<Person> secondDegreeConnectionsList = connectionsService.getSecondDegreeConnectionsOfUser(userId);
        return new ResponseEntity<>(secondDegreeConnectionsList, HttpStatus.OK);
    }

    @GetMapping("/{userId}/third-degree")
    public ResponseEntity<List<Person>> getThirdDegreeConnections(@PathVariable Long userId){
        log.info("Received request to get third degree connections for user with ID: {}", userId);
        List<Person> thirdDegreeConnectionsList = connectionsService.getThirdDegreeConnectionsOfUser(userId);
        return new ResponseEntity<>(thirdDegreeConnectionsList, HttpStatus.OK);
    }

    @PostMapping("/request/{userId}")
    public ResponseEntity<Void> sendConnectionRequest(@PathVariable Long userId){
        connectionsService.sendConnectionRequest(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/accept/{userId}")
    public ResponseEntity<Void> acceptConnectionRequest(@PathVariable Long userId){
        connectionsService.acceptConnectionRequest(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reject/{userId}")
    public ResponseEntity<Void> rejectConnectionRequest(@PathVariable Long userId){
        connectionsService.rejectConnectionRequest(userId);
        return ResponseEntity.noContent().build();
    }

}
