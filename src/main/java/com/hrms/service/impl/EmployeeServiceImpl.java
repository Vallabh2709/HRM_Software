package com.hrms.service.impl;

import com.hrms.dto.request.CreateEmployeeRequest;
import com.hrms.dto.response.EmployeeResponse;
import com.hrms.entity.*;
import com.hrms.exception.ResourceAlreadyExistsException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.DepartmentRepository;
import com.hrms.repository.DesignationRepository;
import com.hrms.repository.EmployeeRepository;
import com.hrms.service.EmployeeService;
import com.hrms.service.UserService;
import com.hrms.service.generator.EmployeeCodeGenerator;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.hrms.exception.InvalidRequestException;
import java.util.List;
import com.hrms.dto.request.UpdateEmployeeRequest;
import com.hrms.dto.internal.CreateUserResult;
import com.hrms.dto.response.CreateEmployeeResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final UserService userService;
    private final EmployeeCodeGenerator employeeCodeGenerator;


    @Override
    public CreateEmployeeResponse createEmployee(CreateEmployeeRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found."));

        if (!department.getStatus()) {
            throw new InvalidRequestException(
                    "Cannot assign an employee to an inactive department."
            );
        }
        Designation designation = designationRepository.findById(request.getDesignationId())
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found."));

        if (!designation.getStatus()) {
            throw new InvalidRequestException(
                    "Cannot assign an employee to an inactive designation."
            );
        }


        if (!designation.getDepartment().getId().equals(department.getId())) {
            throw new InvalidRequestException(
                    "The selected designation does not belong to the selected department."
            );
        }

        if (employeeRepository.existsByPhone(request.getPhone())) {
            throw new ResourceAlreadyExistsException("Phone number already exists.");
        }
// Create login account and generate temporary password
        CreateUserResult createUserResult = userService.createUser(
                request.getEmail(),
                Role.EMPLOYEE
        );

// Extract the saved user
        User user = createUserResult.getUser();
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

        Employee savedEmployee = employeeRepository.save(employee);

        return mapToCreateResponse(
                savedEmployee,
                createUserResult.getTemporaryPassword()
        );
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found."));

        return mapToResponse(employee);
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {

        return employeeRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private EmployeeResponse mapToResponse(Employee employee) {

        return EmployeeResponse.builder()
                .id(employee.getId())
                .employeeCode(employee.getEmployeeCode())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getUser().getEmail())
                .phone(employee.getPhone())
                .dateOfBirth(employee.getDateOfBirth())
                .gender(employee.getGender())
                .joiningDate(employee.getJoiningDate())
                .active(employee.isActive())
                .departmentName(employee.getDepartment().getName())
                .designationName(employee.getDesignation().getName())
                .build();
    }


    private CreateEmployeeResponse mapToCreateResponse(
            Employee employee,
            String temporaryPassword) {

        return CreateEmployeeResponse.builder()
                .id(employee.getId())
                .employeeCode(employee.getEmployeeCode())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getUser().getEmail())
                .phone(employee.getPhone())
                .dateOfBirth(employee.getDateOfBirth())
                .gender(employee.getGender())
                .joiningDate(employee.getJoiningDate())
                .active(employee.isActive())
                .departmentName(employee.getDepartment().getName())
                .designationName(employee.getDesignation().getName())

                // Return only once during employee creation
                .temporaryPassword(temporaryPassword)

                .build();
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, UpdateEmployeeRequest request) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found."));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found."));

        if (!department.getStatus()) {
            throw new InvalidRequestException(
                    "Cannot assign an employee to an inactive department."
            );
        }

        Designation designation = designationRepository.findById(request.getDesignationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Designation not found."));

        if (!designation.getStatus()) {
            throw new InvalidRequestException(
                    "Cannot assign an employee to an inactive designation."
            );
        }

        if (!designation.getDepartment().getId().equals(department.getId())) {
            throw new InvalidRequestException(
                    "The selected designation does not belong to the selected department."
            );
        }

        if (employeeRepository.existsByPhoneAndIdNot(request.getPhone(), id)) {
            throw new ResourceAlreadyExistsException(
                    "Phone number already exists."
            );
        }

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setPhone(request.getPhone());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setGender(request.getGender());
        employee.setJoiningDate(request.getJoiningDate());
        employee.setDepartment(department);
        employee.setDesignation(designation);

        Employee updatedEmployee = employeeRepository.save(employee);

        return mapToResponse(updatedEmployee);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {

        // Step 1: Find employee
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found."));

        // Step 2: Check if already inactive
        if (!employee.isActive())  {
            throw new InvalidRequestException("Employee is already inactive.");
        }

        // Step 3: Soft delete employee
        employee.setActive(false);

        // Step 4: Disable login
        employee.getUser().setEnabled(false);

        // Step 5: Save
        employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public void restoreEmployee(Long id) {

        // Step 1: Find employee
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found."));

        // Step 2: Check if already active
        if (employee.isActive()) {
            throw new InvalidRequestException("Employee is already active.");
        }

        // Step 3: Restore employee
        employee.setActive(true);

        // Step 4: Enable login
        employee.getUser().setEnabled(true);

        // Step 5: Save
        employeeRepository.save(employee);
    }


    @Override
    public EmployeeResponse getMyProfile() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Employee employee = employeeRepository.findByUserEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found."));

        return mapToResponse(employee);
    }
}