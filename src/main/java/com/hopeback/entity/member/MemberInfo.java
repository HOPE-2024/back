package com.hopeback.entity.member;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "member_info")
@Getter
@Setter
@NoArgsConstructor
public class MemberInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // @ManyToOne 관계로 변경
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "height")
    private Double height;

    @Column(name = "weight")
    private Double weight;
}
