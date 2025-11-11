package com.spring.boot.job.tracker.app.repository;

import com.spring.boot.job.tracker.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // 🔹 Find by username
    Optional<User> findByUsername(String username);

    // 🔹 Find by username or email
    @Query("SELECT u FROM User u WHERE u.username = :login OR u.email = :login")
    Optional<User> findByUsernameOrEmail(@Param("login") String login);

    // 🔹 Find by email
    Optional<User> findByEmail(String email);

    // 🔹 Check if email exists (for signup validation)
    boolean existsByEmail(String email);

    // 🔹 Check if username exists
    boolean existsByUsername(String username);

    // 🔹 Find active user by email
    Optional<User> findByEmailAndIsActiveTrue(String email);

    // 🔹 Example of custom query (native or JPQL)
    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.accountLocked = false")
    Iterable<User> findAllActiveUnlockedUsers();
}
