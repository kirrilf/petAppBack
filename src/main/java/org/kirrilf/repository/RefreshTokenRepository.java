package org.kirrilf.repository;

import org.kirrilf.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByFingerprint(String fingerprint);
}
