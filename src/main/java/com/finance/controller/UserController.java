package com.finance.controller;

import com.finance.access.RequiresRole;
import com.finance.dto.UserRequest;
import com.finance.dto.UserResponse;
import com.finance.model.Role;
import com.finance.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // POST /api/users
    // Only ADMIN can create users
    @PostMapping
    @RequiresRole({Role.ADMIN})
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/users
    // Only ADMIN can view all users
    @GetMapping
    @RequiresRole({Role.ADMIN})
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // GET /api/users/{id}
    // Only ADMIN can view a specific user
    @GetMapping("/{id}")
    @RequiresRole({Role.ADMIN})
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // PUT /api/users/{id}
    // Only ADMIN can update users
    @PutMapping("/{id}")
    @RequiresRole({Role.ADMIN})
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                   @Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    // PATCH /api/users/{id}/activate
    // Only ADMIN can activate users
    @PatchMapping("/{id}/activate")
    @RequiresRole({Role.ADMIN})
    public ResponseEntity<UserResponse> activateUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.activateUser(id));
    }

    // PATCH /api/users/{id}/deactivate
    // Only ADMIN can deactivate users
    @PatchMapping("/{id}/deactivate")
    @RequiresRole({Role.ADMIN})
    public ResponseEntity<UserResponse> deactivateUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deactivateUser(id));
    }

    // PATCH /api/users/{id}/role?roleName=ANALYST
    // Only ADMIN can assign roles
    @PatchMapping("/{id}/role")
    @RequiresRole({Role.ADMIN})
    public ResponseEntity<UserResponse> assignRole(@PathVariable Long id,
                                                   @RequestParam String roleName) {
        return ResponseEntity.ok(userService.assignRole(id, roleName));
    }
}
