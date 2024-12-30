package org.ucentralasia.NovelNest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ucentralasia.NovelNest.model.user.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}
