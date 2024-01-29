package com.hopeback.dto.admin;

import com.hopeback.entity.member.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
    private Long id;
    private Member reporter;
    private Member reported;
    private String check;
    private String reason;
    private LocalDateTime date;
    private String status;

    // Constructors, getters, setters can be added as needed
}