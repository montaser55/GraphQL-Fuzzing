package com.pm.userservice.service;

import com.pm.userservice.dto.UserCreationOrUpdateRequestDTO;
import com.pm.userservice.dto.UserResponseDTO;
import com.pm.userservice.exception.EmailAlreadyExistsException;
import com.pm.userservice.exception.UserNotFoundException;
import com.pm.userservice.mapper.UserMapper;
import com.pm.userservice.model.User;
import com.pm.userservice.model.UserRole;
import com.pm.userservice.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO createUser(UserCreationOrUpdateRequestDTO userCreationOrUpdateRequestDTO) {
        if (userRepository.existsByEmail(userCreationOrUpdateRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("User with EMail " + userCreationOrUpdateRequestDTO.getEmail() + " already exists.");
        }
        User user = UserMapper.toModel(userCreationOrUpdateRequestDTO);
        user.setIsVerified(false);
        user.setUserRole(UserRole.USER);

        User newUser = userRepository.save(user);
        return UserMapper.toDTO(newUser);
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findByIdOrThrow(id);
        return UserMapper.toDTO(user);
    }

    public UserResponseDTO updateUser(Long id, UserCreationOrUpdateRequestDTO userCreationOrUpdateRequestDTO) {
        User user = userRepository.findByIdOrThrow(id);
        String updatedEmail = userCreationOrUpdateRequestDTO.getEmail();
        if (userRepository.existsByEmailAndIdNot(updatedEmail, id)) {
            throw new EmailAlreadyExistsException("User with Email " + updatedEmail + " already exists.");
        }
        updateUserFields(user, userCreationOrUpdateRequestDTO);
        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);
        return UserMapper.toDTO(updatedUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with ID " + id + " not found.");
        }
        userRepository.deleteById(id);
    }

    public UserResponseDTO searchUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElse(null);

        return user != null ? UserMapper.toDTO(user) : null;
    }

    public UserResponseDTO verifyUser(Long id) {
        User user = userRepository.findByIdOrThrow(id);
        user.setIsVerified(true);
        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);
        return UserMapper.toDTO(updatedUser);
    }


    private void updateUserFields(User user,
                                  UserCreationOrUpdateRequestDTO userCreationOrUpdateRequestDTO) {
        user.setName(userCreationOrUpdateRequestDTO.getName());
        user.setEmail(userCreationOrUpdateRequestDTO.getEmail());
        user.setAge(userCreationOrUpdateRequestDTO.getAge());
        user.setGender(userCreationOrUpdateRequestDTO.getGender());
        user.setAddress(userCreationOrUpdateRequestDTO.getAddress());
    }
}
