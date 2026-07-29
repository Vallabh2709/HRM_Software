package com.hrms.dto.response;

import com.hrms.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeResponse {

    private Long id;

    private String employeeCode;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private LocalDate dateOfBirth;

    private Gender gender;

    private LocalDate joiningDate;

    private boolean active;

    private String departmentName;

    private String designationName;

    /**
     * Temporary password generated during employee creation.
     *
     * This value is returned ONLY once when the employee is created.
     * It is NOT stored in the database.
     * After this response, it cannot be retrieved again.
     */
    private String temporaryPassword;
}