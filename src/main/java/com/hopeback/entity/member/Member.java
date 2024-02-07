package com.hopeback.entity.member;

import com.hopeback.constant.Authority;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
public class Member {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String memberId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = true)
    private String profile;

    private String active;
    private LocalDateTime active_date;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_type", columnDefinition = "ENUM('MEMBER', 'ADMIN') DEFAULT 'MEMBER'")
    private Authority authority;

    // jwt를 위한 빌더 패턴 사용
    @Builder
    public Member(String memberId, String password, String name, String nickName, String email, String phone, String profile, String active, Authority authority) {
        this.memberId = memberId;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.phone = phone;
        this.profile = profile;
        this.active = active;
        this.active_date = LocalDateTime.now();
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", memberId='" + memberId + '\'' +
                ", name='" + name + '\'' +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", active='" + active + '\'' +
                ", active_date=" + active_date +
                ", authority=" + authority +
                '}';
    }

    //비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}
