package com.finance.repository;

import com.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Used to check if email already exists before creating a user
    boolean existsByEmail(String email);

    // Used to look up a user by email (for access control checks)
    Optional<User> findByEmail(String email);
}