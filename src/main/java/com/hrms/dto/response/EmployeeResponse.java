package com.hrms.dto.response;

import com.hrms.enums.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class EmployeeResponse {

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

}