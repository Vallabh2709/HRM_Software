package com.hrms.service;

import com.hrms.dto.request.CreateEmployeeRequest;
import com.hrms.dto.response.EmployeeResponse;
import com.hrms.dto.request.UpdateEmployeeRequest;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(CreateEmployeeRequest request);

    EmployeeResponse getEmployeeById(Long id);

    List<EmployeeResponse> getAllEmployees();

    EmployeeResponse updateEmployee(Long id, UpdateEmployeeRequest request);

    void deleteEmployee(Long id);

    void restoreEmployee(Long id);
}