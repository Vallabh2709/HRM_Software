package com.hrms.service;

import com.hrms.dto.request.BootstrapAdminRequest;
import com.hrms.dto.response.BootstrapAdminResponse;

public interface BootstrapService {

    BootstrapAdminResponse bootstrapAdmin(BootstrapAdminRequest request);

}