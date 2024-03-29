package com.hopeback.dto.member;

import com.hopeback.constant.Authority;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Getter
@Setter
@Component
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    // 클라이언트에세 전송하기 위한 용도
    private Long id;
    private String memberId;
    private String password;
    private String name;
    private String nickName;
    private String email;
    private String phone;
    private String active;
    private LocalDateTime active_date;
    @Enumerated(EnumType.STRING)
    private Authority authority;
    private String image;

    @Builder
    public MemberDto(Long id, String image){
        this.id = id;
        this.image = image;
    }

}
