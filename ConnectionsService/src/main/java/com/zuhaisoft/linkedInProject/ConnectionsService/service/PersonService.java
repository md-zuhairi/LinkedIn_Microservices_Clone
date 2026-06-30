package com.zuhaisoft.linkedInProject.ConnectionsService.service;

import com.zuhaisoft.linkedInProject.ConnectionsService.entity.Person;
import com.zuhaisoft.linkedInProject.ConnectionsService.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {
    private final PersonRepository personRepository;

    @Transactional
    public void createPerson(Long userId, String name){
        Person person = Person.builder()
                .name(name)
                .userId(userId)
                .build();
        personRepository.save(person);
    }

}
