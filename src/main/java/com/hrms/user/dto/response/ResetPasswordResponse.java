package com.hrms.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Reset Password Response")
public class ResetPasswordResponse {

    @Schema(example = "admin@hrms.com")
    private String email;

    @Schema(example = "Ab@9LpQx")
    private String temporaryPassword;

}