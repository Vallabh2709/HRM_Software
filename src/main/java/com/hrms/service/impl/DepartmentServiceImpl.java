package com.hrms.service.impl;

import com.hrms.dto.request.CreateDepartmentRequest;
import com.hrms.dto.request.UpdateDepartmentRequest;
import com.hrms.dto.response.DepartmentResponse;
import com.hrms.entity.Department;
import com.hrms.exception.ResourceAlreadyExistsException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.DepartmentRepository;
import com.hrms.repository.DesignationRepository;
import com.hrms.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;

    @Override
    public DepartmentResponse createDepartment(CreateDepartmentRequest request) {

        Department existingDepartment = departmentRepository
                .findByNameIgnoreCase(request.getName())
                .orElse(null);

        if (existingDepartment != null) {

            if (existingDepartment.getStatus()) {
                throw new ResourceAlreadyExistsException("Department already exists.");
            }

            // Restore soft deleted department
            existingDepartment.setStatus(true);
            existingDepartment.setDescription(request.getDescription());

            Department restoredDepartment = departmentRepository.save(existingDepartment);

            return mapToResponse(restoredDepartment);
        }

        Department department = new Department();
        department.setName(request.getName());
        department.setDescription(request.getDescription());

        Department savedDepartment = departmentRepository.save(department);

        return mapToResponse(savedDepartment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {

        return departmentRepository.findByStatusTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found."));

        return mapToResponse(department);
    }

    @Override
    public DepartmentResponse updateDepartment(
            Long id,
            UpdateDepartmentRequest request) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found."));

        if (departmentRepository.existsByNameIgnoreCaseAndIdNot(
                request.getName(), id)) {

            throw new ResourceAlreadyExistsException("Department already exists.");
        }

        department.setName(request.getName());
        department.setDescription(request.getDescription());

        Department updatedDepartment = departmentRepository.save(department);

        return mapToResponse(updatedDepartment);
    }

    @Override
    public void deleteDepartment(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found."));

        if (!department.getStatus()) {
            throw new ResourceNotFoundException("Department already deleted.");
        }

        if (designationRepository.existsByDepartmentAndStatusTrue(department)) {
            throw new ResourceAlreadyExistsException(
                    "Department cannot be deleted because it has active designations.");
        }

        department.setStatus(false);

        departmentRepository.save(department);
    }

    private DepartmentResponse mapToResponse(Department department) {

        DepartmentResponse response = new DepartmentResponse();

        response.setId(department.getId());
        response.setName(department.getName());
        response.setDescription(department.getDescription());
        response.setStatus(department.getStatus());
        response.setCreatedAt(department.getCreatedAt());
        response.setUpdatedAt(department.getUpdatedAt());

        return response;
    }
}