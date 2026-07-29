package com.hrms.dto.request;

import com.hrms.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BootstrapAdminRequest {

    @NotBlank(message = "First name is required.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    @Email(message = "Invalid email format.")
    @NotBlank(message = "Email is required.")
    private String email;

    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must contain exactly 10 digits."
    )
    private String phone;

    @Past(message = "Date of birth must be in the past.")
    @NotNull(message = "Date of birth is required.")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required.")
    private Gender gender;

    @NotNull(message = "Joining date is required.")
    private LocalDate joiningDate;
}