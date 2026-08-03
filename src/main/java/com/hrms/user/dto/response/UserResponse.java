package com.hrms.user.dto.response;

import com.hrms.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "User Response")
public class UserResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "admin@hrms.com")
    private String email;

    @Schema(example = "ADMIN")
    private Role role;

    @Schema(example = "true")
    private boolean enabled;

    @Schema(example = "true")
    private boolean passwordChanged;

    @Schema(example = "EMP001")
    private String employeeCode;

    @Schema(example = "John Doe")
    private String employeeName;

}