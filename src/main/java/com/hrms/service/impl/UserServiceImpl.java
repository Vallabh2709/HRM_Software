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
import com.hrms.dto.internal.CreateUserResult;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // Inject PasswordEncoder bean from SecurityConfig
    // Spring will automatically provide the BCryptPasswordEncoder bean
    private final PasswordEncoder passwordEncoder;

    @Override
    public CreateUserResult createEmployeeUser(String email) {

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
                .role(Role.EMPLOYEE)
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
     * Example: A1B2C3D4
     *
     * This password will be encrypted before storing in the database.
     */
    private String generateTemporaryPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}