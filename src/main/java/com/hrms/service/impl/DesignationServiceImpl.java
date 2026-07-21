package com.hrms.service.impl;

import com.hrms.dto.request.CreateDesignationRequest;
import com.hrms.dto.response.DesignationResponse;
import com.hrms.entity.Department;
import com.hrms.entity.Designation;
import com.hrms.exception.ResourceAlreadyExistsException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.DepartmentRepository;
import com.hrms.repository.DesignationRepository;
import com.hrms.service.DesignationService;
import org.springframework.stereotype.Service;
import com.hrms.dto.request.CreateDesignationRequest;
import com.hrms.dto.request.UpdateDesignationRequest;

import java.util.List;

@Service
public class DesignationServiceImpl implements DesignationService {

    private final DesignationRepository designationRepository;
    private final DepartmentRepository departmentRepository;

    public DesignationServiceImpl(
            DesignationRepository designationRepository,
            DepartmentRepository departmentRepository) {

        this.designationRepository = designationRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public DesignationResponse createDesignation(CreateDesignationRequest request) {

        // Check if department exists
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found."));

        // Check duplicate designation in same department
        if (designationRepository.existsByNameAndDepartment(
                request.getName(), department)) {

            throw new ResourceAlreadyExistsException(
                    "Designation already exists in this department.");
        }

        // Create designation
        Designation designation = new Designation();
        designation.setDepartment(department);
        designation.setName(request.getName());
        designation.setDescription(request.getDescription());

        // Save designation
        Designation savedDesignation = designationRepository.save(designation);

        return mapToResponse(savedDesignation);
    }

    @Override
    public List<DesignationResponse> getAllDesignations() {

        List<Designation> designations = designationRepository.findByStatusTrue();

        return designations.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public DesignationResponse getDesignationById(Long id) {

        Designation designation = designationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Designation not found."));

        return mapToResponse(designation);
    }

    /**
     * Converts Designation Entity to DesignationResponse DTO
     */
    private DesignationResponse mapToResponse(Designation designation) {

        DesignationResponse response = new DesignationResponse();

        response.setId(designation.getId());
        response.setDepartmentId(designation.getDepartment().getId());
        response.setDepartmentName(designation.getDepartment().getName());
        response.setName(designation.getName());
        response.setDescription(designation.getDescription());
        response.setStatus(designation.getStatus());
        response.setCreatedAt(designation.getCreatedAt());
        response.setUpdatedAt(designation.getUpdatedAt());

        return response;
    }


    @Override
    public DesignationResponse updateDesignation(
            Long id,
            UpdateDesignationRequest request) {

        // Check if designation exists
        Designation designation = designationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Designation not found."));

        // Check if department exists
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found."));

        // Check duplicate designation
        if (designationRepository.existsByNameAndDepartmentAndIdNot(
                request.getName(),
                department,
                id)) {

            throw new ResourceAlreadyExistsException(
                    "Designation already exists in this department.");
        }

        // Update fields
        designation.setDepartment(department);
        designation.setName(request.getName());
        designation.setDescription(request.getDescription());

        // Save updated designation
        Designation updatedDesignation = designationRepository.save(designation);

        return mapToResponse(updatedDesignation);
    }
    @Override
    public void deleteDesignation(Long id) {

        Designation designation = designationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Designation not found."));

        if (!designation.getStatus()) {
            throw new ResourceNotFoundException("Designation already deleted.");
        }

        designation.setStatus(false);

        designationRepository.save(designation);
    }
}