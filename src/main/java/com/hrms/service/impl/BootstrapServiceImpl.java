package com.hrms.service.impl;

import com.hrms.dto.internal.CreateUserResult;
import com.hrms.dto.request.BootstrapAdminRequest;
import com.hrms.dto.response.BootstrapAdminResponse;
import com.hrms.entity.Department;
import com.hrms.entity.Designation;
import com.hrms.entity.Employee;
import com.hrms.enums.Role;
import com.hrms.entity.User;
import com.hrms.exception.InvalidRequestException;
import com.hrms.exception.ResourceAlreadyExistsException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.DepartmentRepository;
import com.hrms.repository.DesignationRepository;
import com.hrms.repository.EmployeeRepository;
import com.hrms.repository.UserRepository;
import com.hrms.service.BootstrapService;
import com.hrms.service.UserService;
import com.hrms.service.generator.EmployeeCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BootstrapServiceImpl implements BootstrapService {

    private static final String SYSTEM_ADMIN_DEPARTMENT = "System Administration";
    private static final String SYSTEM_ADMIN_DESIGNATION = "System Administrator";

    private final UserRepository userRepository;
    private final UserService userService;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeCodeGenerator employeeCodeGenerator;

    @Override
    public BootstrapAdminResponse bootstrapAdmin(BootstrapAdminRequest request) {

        // Check whether an admin already exists
        if (userRepository.existsByRole(Role.ADMIN)) {
            throw new ResourceAlreadyExistsException(
                    "Admin account already exists.");
        }

        // Find Department
        Department department = departmentRepository
                .findByName(SYSTEM_ADMIN_DEPARTMENT)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "System Administration department not found."));

        // Find Designation
        Designation designation = designationRepository
                .findByName(SYSTEM_ADMIN_DESIGNATION)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "System Administrator designation not found."));

        // Validate department/designation status
        if (!department.getStatus()) {
            throw new InvalidRequestException(
                    "System Administration department is inactive.");
        }

        if (!designation.getStatus()) {
            throw new InvalidRequestException(
                    "System Administrator designation is inactive.");
        }
        if (!designation.getDepartment().getId().equals(department.getId())) {
            throw new InvalidRequestException(
                    "System Administrator designation does not belong to the System Administration department.");
        }
        // Create ADMIN user
        CreateUserResult createUserResult =
                userService.createUser(request.getEmail(), Role.ADMIN);

        User user = createUserResult.getUser();

        // Create Employee
        Employee employee = Employee.builder()
                .employeeCode(employeeCodeGenerator.generateEmployeeCode())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .joiningDate(request.getJoiningDate())
                .department(department)
                .designation(designation)
                .user(user)
                .build();

        employeeRepository.save(employee);

        return BootstrapAdminResponse.builder()
                .message("Admin created successfully.")
                .email(user.getEmail())
                .temporaryPassword(createUserResult.getTemporaryPassword())
                .build();
    }
}