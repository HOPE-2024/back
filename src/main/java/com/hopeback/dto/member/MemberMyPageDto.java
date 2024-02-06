package com.hopeback.dto.member;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberMyPageDto {
    private Long id;
    private String memberId;
    private String password;
    private String name;
    private String nickName;
    private String email;
    private String phone;
    private String profile;
    private String active;
    private LocalDateTime active_date;
}
