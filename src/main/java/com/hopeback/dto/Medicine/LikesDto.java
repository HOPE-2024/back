package com.hopeback.dto.Medicine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@AllArgsConstructor
@NoArgsConstructor
public class LikesDto {
    private Long id;
    private String memberId; // 사용자의 id
    private String documentId; // 의약품의 도큐먼트 id

}
