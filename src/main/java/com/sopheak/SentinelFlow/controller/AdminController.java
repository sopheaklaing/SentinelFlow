package com.sopheak.SentinelFlow.controller;

import com.sopheak.SentinelFlow.dto.admin.ChangeRoleRequest;
import com.sopheak.SentinelFlow.dto.admin.ChangeStatusRequest;
import com.sopheak.SentinelFlow.dto.UserResponse;
import com.sopheak.SentinelFlow.entity.Role;
import com.sopheak.SentinelFlow.entity.User;
import com.sopheak.SentinelFlow.exception.ResourceNotFoundException;
import com.sopheak.SentinelFlow.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        List<UserResponse> users =
                userRepository.findAll()
                        .stream()
                        .map(this::toUserResponse)
                        .toList();

        return ResponseEntity.ok(users);
    }


    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id
    ) {

        User user = findUserById(id);

        return ResponseEntity.ok(
                toUserResponse(user)
        );
    }

    @PatchMapping("/users/{id}/role")
    public ResponseEntity<UserResponse> changeUserRole(
            @PathVariable Long id,
            @RequestBody ChangeRoleRequest request
    ) {

        User user = findUserById(id);

        // Never modify existing SUPER_ADMIN
        if (user.getRole() == Role.SUPER_ADMIN) {

            throw new IllegalArgumentException(
                    "SUPER_ADMIN role cannot be changed"
            );
        }

        // Never assign SUPER_ADMIN through this endpoint
        if (request.role() == Role.SUPER_ADMIN) {

            throw new IllegalArgumentException(
                    "SUPER_ADMIN role cannot be assigned"
            );
        }

        user.setRole(request.role());

        User updatedUser =
                userRepository.save(user);

        return ResponseEntity.ok(
                toUserResponse(updatedUser)
        );
    }

    @PatchMapping("/users/{id}/status")
    public ResponseEntity<UserResponse> changeUserStatus(
            @PathVariable Long id,
            @RequestBody ChangeStatusRequest request
    ) {

        User user = findUserById(id);

        // Never disable SUPER_ADMIN
        if (user.getRole() == Role.SUPER_ADMIN) {

            throw new IllegalArgumentException(
                    "SUPER_ADMIN cannot be disabled"
            );
        }

        user.setEnabled(request.enabled());

        User updatedUser =
                userRepository.save(user);

        return ResponseEntity.ok(
                toUserResponse(updatedUser)
        );
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id
    ) {

        User user = findUserById(id);

        // Never delete SUPER_ADMIN
        if (user.getRole() == Role.SUPER_ADMIN) {

            throw new IllegalArgumentException(
                    "SUPER_ADMIN cannot be deleted"
            );
        }

        userRepository.delete(user);

        return ResponseEntity.noContent().build();
    }

    private User findUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );
    }

    private UserResponse toUserResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}