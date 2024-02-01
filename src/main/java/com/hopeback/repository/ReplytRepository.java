package com.hopeback.repository;

import com.hopeback.entity.admin.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplytRepository extends JpaRepository<Reply, Long> {

}
