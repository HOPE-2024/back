package com.hopeback.repository;

import com.hopeback.entity.member.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByMemberId(String memberId);
    boolean existsByNickName(String nickName);
    boolean existsByEmail(String email);
    Optional<Member> findByMemberId(String memberId);
    Optional<Member> findByNickName(String NickName);
    Optional<Member> findByEmail(String email);
    List<Member> findByActive(String activeStatus);
    List<Member> findByActiveIn(List<String> activeList);
    List<Member> findByActiveIn(List<String> activeList, Pageable pageable);
    List<Member> findByNameContaining(String name);
    List<Member> findByNickNameContaining(String name);
    List<Member> findByMemberIdContaining(String name);
    int countByActiveIn(List<String> activeList);

}
