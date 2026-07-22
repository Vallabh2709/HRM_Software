package com.hrms.service.impl;

import com.hrms.entity.Role;
import com.hrms.entity.User;
import com.hrms.exception.ResourceAlreadyExistsException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.UserRepository;
import com.hrms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createEmployeeUser(String email) {

        if (emailExists(email)) {
            throw new ResourceAlreadyExistsException(
                    "User already exists with email: " + email);
        }

        String temporaryPassword = generateTemporaryPassword();

        User user = User.builder()
                .email(email)
                .password(temporaryPassword)
                .role(Role.EMPLOYEE)
                .enabled(true)
                .passwordChanged(false)
                .build();

        return userRepository.save(user);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User findByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email));
    }

    private String generateTemporaryPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}