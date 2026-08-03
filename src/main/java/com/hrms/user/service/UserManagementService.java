package com.hrms.user.service;

import com.hrms.user.dto.request.ChangeRoleRequest;
import com.hrms.user.dto.response.ResetPasswordResponse;
import com.hrms.user.dto.response.UserResponse;

import java.util.List;

public interface UserManagementService {

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    UserResponse changeRole(Long id,
                            ChangeRoleRequest request);

    void enableUser(Long id);

    void disableUser(Long id);

    ResetPasswordResponse resetPassword(Long id);

}