package com.hopeback.entity.chat;

import com.hopeback.entity.member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "memberSession")
public class MemberSession {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom; // 채팅방과의 관계

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_nickName")
    private Member nickName; // 사용자닉네임

    @Column(name = "session_id")
    private String sessionId; // 세션 ID
}
