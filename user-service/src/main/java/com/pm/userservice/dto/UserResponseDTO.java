package com.pm.userservice.dto;

import com.pm.userservice.model.Gender;
import com.pm.userservice.model.UserRole;

public record UserResponseDTO(
        Long id,
        String name,
        Integer age,
        Gender gender,
        String email,
        String address,
        UserRole userRole,
        Boolean isVerified

){
}
