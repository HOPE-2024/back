package com.hopeback.entity.event;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "event")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String description;
    private LocalDate date; // 일정 날짜
}
