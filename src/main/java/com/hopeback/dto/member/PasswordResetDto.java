package com.hopeback.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PasswordResetDto {
    private String memberId;
    private String password;
}
