package com.hopeback.dto.admin;

import com.hopeback.entity.member.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDto {
    private Long id;
    private String answerer;
    private String answer;
    private Long  queryId;


    // Constructors, getters, setters can be added as needed
}