package com.hrms.controller;

import com.hrms.dto.request.CreateEmployeeRequest;
import com.hrms.dto.response.CreateEmployeeResponse;
import com.hrms.dto.response.EmployeeResponse;
import com.hrms.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hrms.dto.request.UpdateEmployeeRequest;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<CreateEmployeeResponse> createEmployee(
            @Valid @RequestBody CreateEmployeeRequest request) {

        CreateEmployeeResponse response = employeeService.createEmployee(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable Long id) {

        EmployeeResponse response = employeeService.getEmployeeById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {

        List<EmployeeResponse> employees = employeeService.getAllEmployees();

        return ResponseEntity.ok(employees);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmployeeRequest request) {

        EmployeeResponse response = employeeService.updateEmployee(id, request);

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.ok("Employee deleted successfully.");
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<String> restoreEmployee(@PathVariable Long id) {

        employeeService.restoreEmployee(id);

        return ResponseEntity.ok("Employee restored successfully.");
    }

}