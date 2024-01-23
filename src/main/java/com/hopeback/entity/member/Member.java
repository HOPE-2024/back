package com.hopeback.entity.member;

import com.hopeback.constant.Authority;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter @Setter @ToString
@NoArgsConstructor
public class Member {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String member_Id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String nickName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    private int active;
    private LocalDateTime active_date;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_type", columnDefinition = "ENUM('MEMBER', 'ADMIN') DEFAULT 'MEMBER'")
    private Authority authority;

    // jwt를 위한 빌더 패턴 사용
    @Builder
    public Member(String member_Id, String password, String name, String nickName, String email, String phone, int active, Authority authority) {
        this.member_Id = member_Id;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.phone = phone;
        this.active = active;
        this.active_date = LocalDateTime.now();
        this.authority = authority;
    }
}
