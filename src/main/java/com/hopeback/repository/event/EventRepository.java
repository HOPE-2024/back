package com.hopeback.repository.event;

import com.hopeback.entity.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    // Member 엔티티의 memberId를 사용하여 Event 목록 조회
    List<Event> findByMemberMemberId(String memberId);
}
