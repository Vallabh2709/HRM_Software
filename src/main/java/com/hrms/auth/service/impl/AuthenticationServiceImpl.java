package com.hrms.auth.service.impl;

import com.hrms.auth.dto.request.ChangePasswordRequest;
import com.hrms.auth.dto.request.LoginRequest;
import com.hrms.auth.dto.response.LoginResponse;
import com.hrms.auth.dto.response.MessageResponse;
import com.hrms.auth.service.AuthenticationService;
import com.hrms.entity.User;
import com.hrms.exception.InvalidRequestException;
import com.hrms.security.jwt.JwtService;
import com.hrms.security.model.CustomUserDetails;
import com.hrms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        String jwtToken = jwtService.generateToken(userDetails);

        return LoginResponse.builder()
                .message("Login successful")
                .token(jwtToken)
                .passwordChanged(userDetails.getUser().isPasswordChanged())
                .build();
    }

    @Override
    public MessageResponse changePassword(ChangePasswordRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userService.findByEmail(email);

        // Verify current password
        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {

            throw new InvalidRequestException("Current password is incorrect.");
        }

        // Verify new password and confirm password
        if (!request.getNewPassword()
                .equals(request.getConfirmPassword())) {

            throw new InvalidRequestException(
                    "New password and confirm password do not match.");
        }

        // Prevent reusing the same password
        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPassword())) {

            throw new InvalidRequestException(
                    "New password cannot be the same as the current password.");
        }

        String encodedPassword =
                passwordEncoder.encode(request.getNewPassword());

        userService.changePassword(user, encodedPassword);

        return new MessageResponse(
                "Password changed successfully.");
    }
}