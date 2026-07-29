package com.hrms.auth.controller;

import com.hrms.auth.dto.request.LoginRequest;
import com.hrms.auth.dto.response.LoginResponse;
import com.hrms.auth.service.AuthenticationService;
import com.hrms.dto.request.BootstrapAdminRequest;
import com.hrms.dto.response.BootstrapAdminResponse;
import com.hrms.service.BootstrapService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hrms.auth.dto.request.ChangePasswordRequest;
import com.hrms.auth.dto.response.MessageResponse;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final BootstrapService bootstrapService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authenticationService.login(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/bootstrap-admin")
    public ResponseEntity<BootstrapAdminResponse> bootstrapAdmin(
            @Valid @RequestBody BootstrapAdminRequest request) {

        BootstrapAdminResponse response =
                bootstrapService.bootstrapAdmin(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<MessageResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        MessageResponse response =
                authenticationService.changePassword(request);

        return ResponseEntity.ok(response);
    }
}