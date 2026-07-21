package com.hrms.service;

import com.hrms.dto.request.CreateDesignationRequest;
import com.hrms.dto.request.UpdateDesignationRequest;
import com.hrms.dto.response.DesignationResponse;

import java.util.List;

public interface DesignationService {

    DesignationResponse createDesignation(CreateDesignationRequest request);
    List<DesignationResponse> getAllDesignations();
    DesignationResponse getDesignationById(Long id);
    DesignationResponse updateDesignation(
            Long id,
            UpdateDesignationRequest request
    );
    void deleteDesignation(Long id);

}