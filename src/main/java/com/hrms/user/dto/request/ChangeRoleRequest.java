package com.hrms.user.dto.request;

import com.hrms.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Change User Role Request")
public class ChangeRoleRequest {

    @NotNull(message = "Role is required.")
    @Schema(
            description = "New role to assign",
            example = "ADMIN"
    )
    private Role role;

}