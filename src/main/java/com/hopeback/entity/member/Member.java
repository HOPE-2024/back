package com.hopeback.entity.member;

import com.hopeback.constant.Authority;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.io.Serializable;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
public class Member implements Serializable {
    private static final long serialVersionUID = 1L;

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
    private String image;

    // jwt를 위한 빌더 패턴 사용
    @Builder
    public Member(String memberId, String password, String name, String nickName, String email, String phone, String profile, String active, Authority authority, String image) {
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
        this.image = image;
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

    // ? extends GrantedAuthority : GrantedAuthority 인터페이스를 상속한 어떤 클래스든지 받아들일 수 있다는 의미.
    // 이를 사용하여 메서드의 반환 타입이나 매개변수의 타입을 지정할 때, 특정 클래스의 하위 클래스들을 모두 포함할 수 있도록 유연성을 확보할 수 있음.
    public Collection<? extends GrantedAuthority> getAuthority2() {
        return List.of((new SimpleGrantedAuthority("ROLE_MEMBER")));  // SimpleGrantedAuthority : 사용자에게 할당된 권한을 나타냄.
        // List.of : 불변 리스트를 생성. 요소를 추가, 제거하거나 수정하는 메서드가 제공되지 않음. 권한이 변하지 않도록 보장하기 위해 사용됨.
    }

}
