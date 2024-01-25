package com.hopeback.repository;

import com.hopeback.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByMemberId(String memberId);
    boolean existsByNickName(String nickName);
    List<Member> findByActive (int memberId);
}
