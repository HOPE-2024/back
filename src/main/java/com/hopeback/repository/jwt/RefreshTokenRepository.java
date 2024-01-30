package com.hopeback.repository.jwt;

import com.hopeback.entity.jwt.RefreshToken;
import com.hopeback.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository  extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMember(Member member); // 주어진 회원에 해당하는 리프레스 토큰을 찾음.
    Optional<RefreshToken> findByRefreshToken(String refreshToken);  // 리프레시 토큰으로 해당하는 리프레시 토큰을 찾음. 액세스 토큰을 재발급 할 때 사용.
}
