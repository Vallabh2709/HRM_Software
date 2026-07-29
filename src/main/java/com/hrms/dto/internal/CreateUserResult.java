package com.hrms.dto.internal;

import com.hrms.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateUserResult {

    /**
     * Saved user entity.
     */
    private final User user;

    /**
     * Plain temporary password.
     * This is returned only once during employee creation.
     * It is NOT stored in the database.
     */
    private final String temporaryPassword;
}