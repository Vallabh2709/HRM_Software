package com.hrms.auth.service;

import com.hrms.auth.dto.request.LoginRequest;
import com.hrms.auth.dto.response.LoginResponse;

public interface AuthenticationService {

    /**
     * Authenticates a user using email and password.
     *
     * @param request Login credentials
     * @return Login response
     */
    LoginResponse login(LoginRequest request);

}