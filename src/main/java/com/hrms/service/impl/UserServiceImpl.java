package com.hrms.service.impl;

import com.hrms.dto.internal.CreateUserResult;
import com.hrms.entity.Role;
import com.hrms.entity.User;
import com.hrms.exception.ResourceAlreadyExistsException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.UserRepository;
import com.hrms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CreateUserResult createUser(String email, Role role) {

        // Check if the email already exists
        if (emailExists(email)) {
            throw new ResourceAlreadyExistsException(
                    "User already exists with email: " + email);
        }

        // Generate temporary password
        String temporaryPassword = generateTemporaryPassword();

        // Encrypt the password
        String encodedPassword = passwordEncoder.encode(temporaryPassword);

        // Create User entity
        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .role(role)
                .enabled(true)
                .passwordChanged(false)
                .build();

        // Save user
        User savedUser = userRepository.save(user);

        // Return both the saved user and the plain temporary password
        return CreateUserResult.builder()
                .user(savedUser)
                .temporaryPassword(temporaryPassword)
                .build();
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

    /**
     * Generates an 8-character temporary password.
     */
    private String generateTemporaryPassword() {

        return "Temp@" + (1000 + new java.util.Random().nextInt(9000));
    }

    @Override
    public void changePassword(User user, String encodedPassword) {

        user.setPassword(encodedPassword);
        user.setPasswordChanged(true);

        userRepository.save(user);
    }
}