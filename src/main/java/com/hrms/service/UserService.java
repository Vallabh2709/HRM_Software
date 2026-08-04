package com.hrms.service;

import com.hrms.dto.internal.CreateUserResult;
import com.hrms.enums.Role;
import com.hrms.entity.User;

public interface UserService {

    /**
     * Creates a new user with the specified role.
     * Generates a random temporary password,
     * encrypts it using BCrypt,
     * saves the user,
     * and returns the user along with the temporary password.
     */
    CreateUserResult createUser(String email, Role role);

    boolean emailExists(String email);

    User findByEmail(String email);

    void changePassword(User user, String encodedPassword);
}