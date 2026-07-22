package com.hrms.repository;

import com.hrms.entity.Designation;
import com.hrms.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findTopByOrderByIdDesc();

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByPhone(String phone);

    boolean existsByDesignationAndActiveTrue(Designation designation);
}