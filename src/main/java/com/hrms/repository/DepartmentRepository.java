package com.hrms.repository;

import com.hrms.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository
        extends JpaRepository<Department, Long> {

       boolean existsByName(String name);
       List<Department> findByStatusTrue();
    boolean existsByNameAndIdNot(String name, Long id);
}