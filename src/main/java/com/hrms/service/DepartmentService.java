package com.hrms.service;

import com.hrms.dto.request.CreateDepartmentRequest;
import com.hrms.dto.response.DepartmentResponse;

public interface DepartmentService {

    DepartmentResponse createDepartment(CreateDepartmentRequest request);
}
