package com.hrms.service;

import com.hrms.dto.request.CreateDepartmentRequest;
import com.hrms.dto.request.UpdateDepartmentRequest;
import com.hrms.dto.response.DepartmentResponse;

import java.util.List;

public interface DepartmentService {

    DepartmentResponse createDepartment(CreateDepartmentRequest request);
    List<DepartmentResponse> getAllDepartments();
    DepartmentResponse getDepartmentById(Long id);
    DepartmentResponse updateDepartment(
            Long id,
            UpdateDepartmentRequest request
    );
    void deleteDepartment(Long id);
}
