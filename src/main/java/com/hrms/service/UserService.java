package com.hrms.service;

import com.hrms.entity.User;

public interface UserService {

    User createEmployeeUser(String email);

    boolean emailExists(String email);

    User findByEmail(String email);

}