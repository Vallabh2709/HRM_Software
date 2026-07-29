package com.hrms.controller;

import com.hrms.dto.request.CreateDesignationRequest;
import com.hrms.dto.request.UpdateDesignationRequest;
import com.hrms.dto.response.DesignationResponse;
import com.hrms.service.DesignationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(
        name = "Designation Management",
        description = "APIs for managing designations"
)
@RestController
@RequestMapping("/api/designations")
public class DesignationController {

    private final DesignationService designationService;

    public DesignationController(DesignationService designationService) {
        this.designationService = designationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DesignationResponse createDesignation(
            @Valid @RequestBody CreateDesignationRequest request) {

        return designationService.createDesignation(request);
    }

    @GetMapping
    public List<DesignationResponse> getAllDesignations() {
        return designationService.getAllDesignations();
    }

    @GetMapping("/{id}")
    public DesignationResponse getDesignationById(
            @PathVariable Long id) {

        return designationService.getDesignationById(id);
    }

    @PutMapping("/{id}")
    public DesignationResponse updateDesignation(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDesignationRequest request) {

        return designationService.updateDesignation(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDesignation(@PathVariable Long id) {

        designationService.deleteDesignation(id);
    }
}

