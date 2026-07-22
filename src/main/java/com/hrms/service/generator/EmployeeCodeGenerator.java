package com.hrms.service.generator;

import com.hrms.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeCodeGenerator {

    private static final String EMPLOYEE_CODE_PREFIX = "EMP";
    private static final int CODE_PADDING = 4;

    private final EmployeeRepository employeeRepository;

    public String generateEmployeeCode() {

        Long nextEmployeeId = employeeRepository.findTopByOrderByIdDesc()
                .map(employee -> employee.getId() + 1)
                .orElse(1L);

        return EMPLOYEE_CODE_PREFIX + String.format("%0" + CODE_PADDING + "d", nextEmployeeId);
    }

}