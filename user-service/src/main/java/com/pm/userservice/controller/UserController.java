package com.pm.userservice.controller;

import com.pm.userservice.dto.UserCreationOrUpdateRequestDTO;
import com.pm.userservice.dto.UserResponseDTO;
import com.pm.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "Apis for managing users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Create a new User")
    public UserResponseDTO createUser(
            @Valid @RequestBody UserCreationOrUpdateRequestDTO userCreationOrUpdateRequestDTO) {
        return userService.createUser(userCreationOrUpdateRequestDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get User info by ID")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update User info by ID")
    public UserResponseDTO updateUser(@PathVariable Long id,
                                      @Valid @RequestBody UserCreationOrUpdateRequestDTO userCreationOrUpdateRequestDTO) {
        return userService.updateUser(id, userCreationOrUpdateRequestDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete User using ID")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search User using email")
    public UserResponseDTO searchUserByEmail(@RequestParam("email") String email) {
        return userService.searchUserByEmail(email);
    }

    @PostMapping("/{id}/verify")
    @Operation(summary = "Verify a user after creation")
    public UserResponseDTO verifyUser(@PathVariable Long id) {
        return userService.verifyUser(id);
    }
}
