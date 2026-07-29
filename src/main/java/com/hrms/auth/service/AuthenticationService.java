package com.hrms.auth.service;

import com.hrms.auth.dto.request.ChangePasswordRequest;
import com.hrms.auth.dto.request.LoginRequest;
import com.hrms.auth.dto.response.LoginResponse;
import com.hrms.auth.dto.response.MessageResponse;

public interface AuthenticationService {

    /**
     * Authenticates a user using email and password.
     *
     * @param request Login credentials
     * @return Login response
     */
    LoginResponse login(LoginRequest request);
    MessageResponse changePassword(ChangePasswordRequest request);
}