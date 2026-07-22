package com.hrms.repository;

import com.hrms.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findByStatusTrue();

    Optional<Department> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndStatusTrue(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}