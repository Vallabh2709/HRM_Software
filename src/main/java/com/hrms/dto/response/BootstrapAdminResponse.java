package com.hrms.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BootstrapAdminResponse {

    private String message;

    private String email;

    private String temporaryPassword;
}