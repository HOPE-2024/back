package com.hopeback.dto.member;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberInfoDto {
    private Long id;
    private String memberId;
    private LocalDate birthDate;
    private Double height;
    private Double weight;
}
