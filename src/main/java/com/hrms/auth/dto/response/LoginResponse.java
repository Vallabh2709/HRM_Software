package com.hrms.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    /**
     * Success message
     */
    private String message;

    /**
     * JWT Access Token
     */
    private String token;

    /**
     * Token Type
     * Example: Bearer
     */
    @Builder.Default
    private String tokenType = "Bearer";
    private boolean passwordChanged;
}