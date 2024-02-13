package com.hopeback.dto.admin;

import com.hopeback.entity.member.Member;
import lombok.*;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QueryDto {
    private Long id;
    private Member questioner;
    private String title;
    private String division;
    private String substance;
    private String queryImg;
    private String often;
    private String status;
    private List<ReplyDto> reply;  // 모든 리뷰


}