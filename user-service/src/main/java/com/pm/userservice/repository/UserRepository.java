package com.pm.userservice.repository;

import com.pm.userservice.exception.UserNotFoundException;
import com.pm.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
    Optional<User> findByEmail(String email);

    default User findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found."));
    }
}
