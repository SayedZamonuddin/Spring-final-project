package org.ucentralasia.NovelNest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ucentralasia.NovelNest.model.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
