package com.hrms.user.service.impl;

import com.hrms.entity.Employee;
import com.hrms.entity.Role;
import com.hrms.entity.User;
import com.hrms.exception.InvalidRequestException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.UserRepository;
import com.hrms.user.dto.request.ChangeRoleRequest;
import com.hrms.user.dto.response.ResetPasswordResponse;
import com.hrms.user.dto.response.UserResponse;
import com.hrms.user.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> getAllUsers() {

        return userRepository.findAllByOrderByIdAsc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = getUser(id);

        return mapToResponse(user);
    }

    @Override
    public UserResponse changeRole(Long id,
                                   ChangeRoleRequest request) {

        User user = getUser(id);

        if (user.getRole() == request.getRole()) {
            throw new InvalidRequestException(
                    "User already has role " + request.getRole());
        }

        if (user.getRole() == Role.ADMIN &&
                request.getRole() == Role.EMPLOYEE &&
                userRepository.countByRole(Role.ADMIN) == 1) {

            throw new InvalidRequestException(
                    "Cannot change the last ADMIN to EMPLOYEE.");
        }

        user.setRole(request.getRole());

        userRepository.save(user);

        return mapToResponse(user);
    }

    @Override
    public void enableUser(Long id) {

        User user = getUser(id);

        if (user.isEnabled()) {
            throw new InvalidRequestException(
                    "User is already enabled.");
        }

        user.setEnabled(true);

        userRepository.save(user);
    }

    @Override
    public void disableUser(Long id) {

        User user = getUser(id);

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String loggedInEmail = authentication.getName();

        if (user.getEmail().equals(loggedInEmail)) {
            throw new InvalidRequestException(
                    "You cannot disable your own account.");
        }

        if (!user.isEnabled()) {
            throw new InvalidRequestException(
                    "User is already disabled.");
        }

        if (user.getRole() == Role.ADMIN &&
                userRepository.countByRole(Role.ADMIN) == 1) {

            throw new InvalidRequestException(
                    "Cannot disable the last ADMIN.");
        }

        user.setEnabled(false);

        userRepository.save(user);
    }

    @Override
    public ResetPasswordResponse resetPassword(Long id) {

        User user = getUser(id);

        String temporaryPassword = generateTemporaryPassword();

        user.setPassword(
                passwordEncoder.encode(temporaryPassword));

        user.setPasswordChanged(false);

        userRepository.save(user);

        return ResetPasswordResponse.builder()
                .email(user.getEmail())
                .temporaryPassword(temporaryPassword)
                .build();
    }

    // =======================================================

    private User getUser(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id : " + id));
    }

    private UserResponse mapToResponse(User user) {

        Employee employee = user.getEmployee();

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .passwordChanged(user.isPasswordChanged())
                .employeeCode(employee.getEmployeeCode())
                .employeeName(
                        employee.getFirstName()
                                + " "
                                + employee.getLastName())
                .build();
    }

    /**
     * Generates secure temporary password.
     */
    private String generateTemporaryPassword() {

        final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String lower = "abcdefghijklmnopqrstuvwxyz";
        final String digits = "0123456789";
        final String special = "@$!%*?&";

        final String all =
                upper + lower + digits + special;

        SecureRandom random = new SecureRandom();

        StringBuilder password = new StringBuilder();

        password.append(
                upper.charAt(random.nextInt(upper.length())));

        password.append(
                lower.charAt(random.nextInt(lower.length())));

        password.append(
                digits.charAt(random.nextInt(digits.length())));

        password.append(
                special.charAt(random.nextInt(special.length())));

        for (int i = 4; i < 10; i++) {

            password.append(
                    all.charAt(random.nextInt(all.length())));
        }

        char[] chars = password.toString().toCharArray();

        for (int i = chars.length - 1; i > 0; i--) {

            int j = random.nextInt(i + 1);

            char temp = chars[i];

            chars[i] = chars[j];

            chars[j] = temp;
        }

        return new String(chars);
    }

}