package com.hopeback.entity.jwt;

import com.hopeback.entity.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "access_token_exp")
    private Long accessTokenExpiresIn;

    @Lob
    @Column(name = "refresh_token", unique = true)
    private String refreshToken;

    @Column(name = "refresh_token_exp")
    private Long refreshTokenExpiresIn;

    @OneToOne(fetch = FetchType.LAZY)   // RefreshToken 엔티티를 조회할 때 연관된 Member 엔티티는 실제 사용 전까지 로딩하지 않도록 설정
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder   // 복잡한 생성 코들르 단순하고 가독성 좋게 관리하기 위해 빌더 패턴 도입
    private RefreshToken(String accessToken, Long accessTokenExpiresIn, String refreshToken, Long refreshTokenExpiresIn, Member member) {
        this.accessToken = accessToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
        this.member = member;
    }

    public void update(String refreshToken, Long refreshTokenExpiresIn) {
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }
}
