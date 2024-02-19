package com.hopeback.repository;

import com.hopeback.entity.member.Naver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NaverRepository extends JpaRepository<Naver, Long> {
    boolean existsByEmail(String email);
    Optional<Naver> findByEmail(String email);
}
