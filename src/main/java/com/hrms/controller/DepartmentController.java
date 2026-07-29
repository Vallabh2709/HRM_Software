package com.hrms.controller;

import com.hrms.dto.request.CreateDepartmentRequest;
import com.hrms.dto.request.UpdateDepartmentRequest;
import com.hrms.dto.response.DepartmentResponse;
import com.hrms.service.DepartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(
        name = "Department Management",
        description = "APIs for managing departments"
)
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentResponse createDepartment(
            @Valid @RequestBody CreateDepartmentRequest request) {

        return departmentService.createDepartment(request);
    }
    @GetMapping
    public List<DepartmentResponse> getAllDepartments() {

        return departmentService.getAllDepartments();
    }
    @GetMapping("/{id}")
    public DepartmentResponse getDepartmentById(@PathVariable Long id) {

        return departmentService.getDepartmentById(id);
    }
    @PutMapping("/{id}")
    public DepartmentResponse updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDepartmentRequest request) {

        return departmentService.updateDepartment(id, request);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDepartment(@PathVariable Long id) {

        departmentService.deleteDepartment(id);
    }

}