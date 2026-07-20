package com.hrms.controller;

import com.hrms.dto.request.CreateDepartmentRequest;
import com.hrms.dto.response.DepartmentResponse;
import com.hrms.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}