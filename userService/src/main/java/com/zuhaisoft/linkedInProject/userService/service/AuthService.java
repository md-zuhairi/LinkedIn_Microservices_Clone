package com.zuhaisoft.linkedInProject.userService.service;

import com.zuhaisoft.linkedInProject.userService.dto.LoginRequestDto;
import com.zuhaisoft.linkedInProject.userService.dto.SignupRequestDto;
import com.zuhaisoft.linkedInProject.userService.dto.UserDto;
import com.zuhaisoft.linkedInProject.userService.entity.User;
import com.zuhaisoft.linkedInProject.userService.event.UserCreatedEvent;
import com.zuhaisoft.linkedInProject.userService.exception.BadRequestException;
import com.zuhaisoft.linkedInProject.userService.repository.UserRepository;
import com.zuhaisoft.linkedInProject.userService.utils.BCrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final KafkaTemplate<Long, UserCreatedEvent> userCreatedEventKafkaTemplate;

    @Transactional
    public UserDto signUp(SignupRequestDto signupRequestDto) {
        log.info("Signup a user with email: {}", signupRequestDto.getEmail());

        boolean exists = userRepository.existsByEmail(signupRequestDto.getEmail());
        if(exists) {
            throw new BadRequestException("User already exists");
        }

        User user = modelMapper.map(signupRequestDto, User.class);
        user.setPassword(BCrypt.hash(signupRequestDto.getPassword()));

        user = userRepository.save(user);

        UserCreatedEvent userCreatedEvent = UserCreatedEvent.builder()
                .userId(user.getId())
                .name(user.getName())
                .build();

        try {
            userCreatedEventKafkaTemplate.send("user_created_topic", userCreatedEvent).get();
            log.info("✅ Message Successfully Acknowledged BY Kafka Broker");
        } catch (Exception e) {
            log.error("❌ Kafka Network Failure: {}", e.getMessage());
            throw new RuntimeException("[-] Kafka Failed to send notification [UserCreated]");
        }

        return modelMapper.map(user, UserDto.class);
    }

    public String login(LoginRequestDto loginRequestDto) {
        log.info("Login request for user with email: {}", loginRequestDto.getEmail());

        User user = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(() -> new BadRequestException(
                "Incorrect email or password"));

        boolean isPasswordMatch = BCrypt.match(loginRequestDto.getPassword(), user.getPassword());

        if(!isPasswordMatch) {
            throw new BadRequestException("Incorrect email or password");
        }

        return jwtService.generateAccessToken(user);
    }
}
