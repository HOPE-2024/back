package com.hopeback.repository;

import com.hopeback.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByMemberId(String memberId);
    boolean existsByNickName(String nickName);
    Optional<Member> findByMemberId(String memberId);
    List<Member> findByActive (String memberId);
    List<Member> findByActiveIn(List<String> activeList);
    List<Member> findByNameContaining(String name);
    List<Member> findByNickNameContaining(String name);
    List<Member> findByMemberIdContaining(String name);




}
