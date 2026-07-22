package com.hrms.repository;

import com.hrms.entity.Department;
import com.hrms.entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DesignationRepository
        extends JpaRepository<Designation, Long> {

    Optional<Designation> findByDepartmentAndNameIgnoreCase(
            Department department,
            String name
    );

    boolean existsByDepartmentAndNameIgnoreCaseAndIdNot(
            Department department,
            String name,
            Long id
    );

    List<Designation> findByStatusTrue();

    boolean existsByDepartmentAndStatusTrue(
            Department department
    );
}