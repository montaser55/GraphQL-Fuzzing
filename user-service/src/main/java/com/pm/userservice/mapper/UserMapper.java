package com.pm.userservice.mapper;

import com.pm.userservice.dto.UserCreationOrUpdateRequestDTO;
import com.pm.userservice.dto.UserResponseDTO;
import com.pm.userservice.model.User;

import java.time.LocalDateTime;

public class UserMapper {

    public static User toModel(
            UserCreationOrUpdateRequestDTO userCreationOrUpdateRequestDTO){

        User user = new User();
        user.setName(userCreationOrUpdateRequestDTO.getName());
        user.setEmail(userCreationOrUpdateRequestDTO.getEmail());
        user.setGender(userCreationOrUpdateRequestDTO.getGender());
        user.setAge(userCreationOrUpdateRequestDTO.getAge());
        user.setAddress(userCreationOrUpdateRequestDTO.getAddress());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

    public static UserResponseDTO toDTO(User user){
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getAge(),
                user.getGender(),
                user.getEmail(),
                user.getAddress(),
                user.getUserRole(),
                user.getIsVerified()
        );
    }
}
