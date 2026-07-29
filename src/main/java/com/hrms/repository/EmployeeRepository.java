package com.hrms.repository;

import com.hrms.entity.Designation;
import com.hrms.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findTopByOrderByIdDesc();

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByPhone(String phone);

    boolean existsByPhoneAndIdNot(String phone, Long id);

    boolean existsByDesignationAndActiveTrue(Designation designation);

    List<Employee> findByActiveTrue();

    Optional<Employee> findByUserEmail(String email);
}