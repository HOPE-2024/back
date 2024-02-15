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
public class MedicineDataDto {
    private String id;
    private String code;
    private String name;
    private String name_en;
    private String company;
    private String company_en;
    private String general;
    private String appearance;
    private String ingredient;
    private String ingredient_en;
    private String method;
    private String period;
    private String insurance;
    private String additive;
    private String image;
    private String effect;
    private String usages;
    private String precautions;
}
