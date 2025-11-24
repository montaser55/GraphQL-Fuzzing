package com.pm.userservice.controller;

import com.pm.userservice.dto.UserCreationOrUpdateRequestDTO;
import com.pm.userservice.dto.UserResponseDTO;
import com.pm.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class UserGraphQLController {

    private final UserService userService;

    public UserGraphQLController(UserService userService) {
        this.userService = userService;
    }

    @MutationMapping
    public UserResponseDTO createUser(@Valid @Argument UserCreationOrUpdateRequestDTO input) {
        return userService.createUser(input);
    }

    @QueryMapping
    public UserResponseDTO getUserById(@Argument Long id) {
        return userService.getUserById(id);
    }

    @MutationMapping
    public UserResponseDTO updateUser(@Argument Long id,
                                      @Valid @Argument UserCreationOrUpdateRequestDTO input) {
        return userService.updateUser(id, input);
    }

    @QueryMapping
    public UserResponseDTO searchUserByEmail(@Argument String email) {
        return userService.searchUserByEmail(email);
    }

    @MutationMapping
    public Boolean deleteUser(@Argument Long id) {
        userService.deleteUser(id);
        return true;
    }

    @MutationMapping
    public UserResponseDTO verifyUser(@Argument Long id) {
        return userService.verifyUser(id);
    }
}
