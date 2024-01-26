package com.hopeback.repository;

import com.hopeback.entity.jwt.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository  extends JpaRepository<RefreshToken, Long> {
}
