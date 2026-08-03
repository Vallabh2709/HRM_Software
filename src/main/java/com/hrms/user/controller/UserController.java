package com.hrms.user.controller;

import com.hrms.user.dto.request.ChangeRoleRequest;
import com.hrms.user.dto.response.ResetPasswordResponse;
import com.hrms.user.dto.response.UserResponse;
import com.hrms.user.service.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(
        name = "User Management",
        description = "APIs for managing system users"
)
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserManagementService userManagementService;

    @Operation(summary = "Get All Users")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Users fetched successfully")
    })
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(
                userManagementService.getAllUsers()
        );
    }

    @Operation(summary = "Get User By Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "User fetched successfully"),
            @ApiResponse(responseCode = "404",
                    description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userManagementService.getUserById(id)
        );
    }

    @Operation(summary = "Change User Role")
    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponse> changeRole(
            @PathVariable Long id,
            @Valid @RequestBody ChangeRoleRequest request) {

        return ResponseEntity.ok(
                userManagementService.changeRole(id, request)
        );
    }

    @Operation(summary = "Enable User")
    @PatchMapping("/{id}/enable")
    public ResponseEntity<String> enableUser(
            @PathVariable Long id) {

        userManagementService.enableUser(id);

        return ResponseEntity.ok(
                "User enabled successfully."
        );
    }

    @Operation(summary = "Disable User")
    @PatchMapping("/{id}/disable")
    public ResponseEntity<String> disableUser(
            @PathVariable Long id) {

        userManagementService.disableUser(id);

        return ResponseEntity.ok(
                "User disabled successfully."
        );
    }

    @Operation(summary = "Reset User Password")
    @PatchMapping("/{id}/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userManagementService.resetPassword(id)
        );
    }

}