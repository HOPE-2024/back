package com.hopeback.repository.event;

import com.hopeback.entity.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
