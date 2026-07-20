package com.hrms.service.impl;

import com.hrms.dto.request.CreateDepartmentRequest;
import com.hrms.dto.response.DepartmentResponse;
import com.hrms.entity.Department;
import com.hrms.repository.DepartmentRepository;
import com.hrms.service.DepartmentService;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public DepartmentResponse createDepartment(CreateDepartmentRequest request) {

        if (departmentRepository.existsByName(request.getName())) {
            throw new RuntimeException("Department already exists.");
        }
        Department department = new Department();
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        Department savedDepartment = departmentRepository.save(department);
        DepartmentResponse response = new DepartmentResponse();

        response.setId(savedDepartment.getId());
        response.setName(savedDepartment.getName());
        response.setDescription(savedDepartment.getDescription());
        response.setStatus(savedDepartment.getStatus());
        response.setCreatedAt(savedDepartment.getCreatedAt());
        response.setUpdatedAt(savedDepartment.getUpdatedAt());

        return response;
    }
}