package com.hopeback.repository;

import com.hopeback.entity.admin.Query;
import com.hopeback.entity.admin.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueryRepository extends JpaRepository<Query, Long> {

    List<Query> findByQuestionerNickName(String nickName);
    List<Query> findByOftenContaining(String often);
    List<Query> findByQuestionerNickNameAndOftenNot(String nickName, String often);
}
