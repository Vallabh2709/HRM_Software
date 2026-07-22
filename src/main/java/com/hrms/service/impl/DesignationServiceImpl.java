package com.hrms.service.impl;

import com.hrms.dto.request.CreateDesignationRequest;
import com.hrms.dto.request.UpdateDesignationRequest;
import com.hrms.dto.response.DesignationResponse;
import com.hrms.entity.Department;
import com.hrms.entity.Designation;
import com.hrms.exception.InvalidRequestException;
import com.hrms.exception.ResourceAlreadyExistsException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.DepartmentRepository;
import com.hrms.repository.DesignationRepository;
import com.hrms.repository.EmployeeRepository;
import com.hrms.service.DesignationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DesignationServiceImpl implements DesignationService {

    private final DesignationRepository designationRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public DesignationResponse createDesignation(CreateDesignationRequest request) {

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found."));

        Designation existingDesignation = designationRepository
                .findByDepartmentAndNameIgnoreCase(
                        department,
                        request.getName())
                .orElse(null);

        if (existingDesignation != null) {

            if (existingDesignation.getStatus()) {
                throw new ResourceAlreadyExistsException(
                        "Designation already exists in this department.");
            }

            existingDesignation.setStatus(true);
            existingDesignation.setDescription(request.getDescription());

            Designation restoredDesignation =
                    designationRepository.save(existingDesignation);

            return mapToResponse(restoredDesignation);
        }

        Designation designation = new Designation();
        designation.setDepartment(department);
        designation.setName(request.getName());
        designation.setDescription(request.getDescription());

        Designation savedDesignation =
                designationRepository.save(designation);

        return mapToResponse(savedDesignation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DesignationResponse> getAllDesignations() {

        return designationRepository.findByStatusTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DesignationResponse getDesignationById(Long id) {

        Designation designation = designationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Designation not found."));

        return mapToResponse(designation);
    }

    @Override
    public DesignationResponse updateDesignation(
            Long id,
            UpdateDesignationRequest request) {

        Designation designation = designationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Designation not found."));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found."));

        if (designationRepository.existsByDepartmentAndNameIgnoreCaseAndIdNot(
                department,
                request.getName(),
                id)) {

            throw new ResourceAlreadyExistsException(
                    "Designation already exists in this department.");
        }

        designation.setDepartment(department);
        designation.setName(request.getName());
        designation.setDescription(request.getDescription());

        Designation updatedDesignation =
                designationRepository.save(designation);

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

        if (employeeRepository.existsByDesignationAndActiveTrue(designation)) {
            throw new InvalidRequestException(
                    "Designation cannot be deleted because it has active employees."
            );
        }

        designation.setStatus(false);

        designationRepository.save(designation);
    }

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
}