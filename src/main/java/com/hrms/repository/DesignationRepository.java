package com.hrms.repository;

import com.hrms.entity.Department;
import com.hrms.entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DesignationRepository extends JpaRepository<Designation, Long> {

    boolean existsByNameAndDepartment(String name, Department department);

    boolean existsByNameAndDepartmentAndIdNot(
            String name,
            Department department,
            Long id
    );

    List<Designation> findByStatusTrue();

    boolean existsByDepartmentAndStatusTrue(Department department);
}