package com.hrms.service;

import com.hrms.dto.internal.CreateUserResult;
import com.hrms.entity.User;

public interface UserService {

    CreateUserResult createEmployeeUser(String email);

    boolean emailExists(String email);

    User findByEmail(String email);

}