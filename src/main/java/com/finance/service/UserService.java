package com.finance.service;

import com.finance.dto.UserRequest;
import com.finance.dto.UserResponse;
import com.finance.exception.DuplicateEmailException;
import com.finance.exception.ResourceNotFoundException;
import com.finance.model.User;
import com.finance.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // --- Create User ---
    public UserResponse createUser(UserRequest request) {

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already in use: " + request.getEmail());
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setActive(true); // new users are active by default

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    // --- Get All Users ---
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // --- Get User By ID ---
    public UserResponse getUserById(Long id) {
        User user = findUserById(id);
        return toResponse(user);
    }

    // --- Update User ---
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = findUserById(id);

        // If email is being changed, make sure new email is not taken
        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already in use: " + request.getEmail());
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    // --- Activate User ---
    public UserResponse activateUser(Long id) {
        User user = findUserById(id);
        user.setActive(true);
        return toResponse(userRepository.save(user));
    }

    // --- Deactivate User ---
    public UserResponse deactivateUser(Long id) {
        User user = findUserById(id);
        user.setActive(false);
        return toResponse(userRepository.save(user));
    }

    // --- Assign Role ---
    public UserResponse assignRole(Long id, String roleName) {
        User user = findUserById(id);

        // Convert string to Role enum safely
        try {
            user.setRole(com.finance.model.Role.valueOf(roleName.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + roleName + ". Must be VIEWER, ANALYST, or ADMIN");
        }

        return toResponse(userRepository.save(user));
    }

    // --- Helper: find user or throw 404 ---
    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    // --- Helper: convert User entity → UserResponse DTO ---
    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.isActive()
        );
    }
}
