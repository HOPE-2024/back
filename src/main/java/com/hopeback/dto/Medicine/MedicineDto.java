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
public class MedicineDto {
    private Long id; // 기본키
    private String documentId; // es의 id 값
    private String name; // es의 name 값

    public MedicineDto(String id, String name) {
        this.documentId = id;
        this.name = name;
    }

}
