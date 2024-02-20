package com.hopeback.entity.event;

import com.hopeback.entity.member.Member;
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

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "memberId") // 기본키 말고 다른 키를 외래키로 참조
    private Member member;
}
