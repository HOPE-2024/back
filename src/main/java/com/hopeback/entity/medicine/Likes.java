package com.hopeback.entity.medicine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hopeback.entity.chat.ChatRoom;
import com.hopeback.entity.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "likes")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Likes {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;
}
