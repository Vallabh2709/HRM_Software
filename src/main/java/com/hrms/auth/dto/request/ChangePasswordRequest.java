package com.hrms.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {

    @NotBlank(message = "Current password is required.")
    private String currentPassword;

    @NotBlank(message = "New password is required.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = """
                    Password must:
                    - Be between 8 and 20 characters
                    - Contain at least one uppercase letter
                    - Contain at least one lowercase letter
                    - Contain at least one digit
                    - Contain at least one special character (@$!%*?&)
                    """
    )
    private String newPassword;

    @NotBlank(message = "Confirm password is required.")
    private String confirmPassword;
}