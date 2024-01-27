package com.hopeback.entity.medicine;


import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;

@Data
@Document(indexName = "medicine")
public class MedicineData {

    // es에서는 문서의 고유 식별자(id)는 기본적으로 문자열(String) 형식으로 설정됨
    // 내부적인 '_id' 값은 UUID 형식으로 생성됨 : 분산 환경에서 고유한 키가 필요한 경우에 사용되며,
    // 중복된 키가 발생활 확률을 거의 제로에 가깝게 줄여줌. 이런 특성으로 인해 es에서는 UUID로 id를 설정하여 문서를 식별함
    @Id
    private String id;

    // type은 지정하지 않아도 es는 동적으로 매핑을 수행하고 필드의 데이터 타입을 자동으로 감지함
    // 그러나 명시적으로 데이터 타입을 지정하고 싶거나, 동적 매핑이 예상과 다르게 동작하는 것을 방지하고 싶다면 type을 지정할 수 있음.
    @Field(name = "code", type = FieldType.Long)
    private String code;

    @Field(name = "name", type = FieldType.Text)
    private String name;

    @Field(name = "name_en", type = FieldType.Text)
    private String name_en;

    @Field(name = "company", type = FieldType.Text)
    private String company;

    @Field(name = "company_en", type = FieldType.Text)
    private String company_en;

    @Field(name = "general", type = FieldType.Text)
    private String general;

    @Field(name = "appearance", type = FieldType.Text)
    private String appearance;

    @Field(name = "ingredient", type = FieldType.Text)
    private String ingredient;

    @Field(name = "ingredient_en", type = FieldType.Text)
    private String ingredient_en;

    @Field(name = "method", type = FieldType.Text)
    private String method;

    @Field(name = "period", type = FieldType.Text)
    private String period;

    @Field(name = "insurance", type = FieldType.Text)
    private String insurance;

    @Field(name = "additive", type = FieldType.Text)
    private String additive;

    @Field(name = "image", type = FieldType.Text)
    private String image;

    @Field(name = "effect", type = FieldType.Text)
    private String effect;

    @Field(name = "usages", type = FieldType.Text)
    private String usages;

    @Field(name = "precautions", type = FieldType.Text)
    private String precautions;
}
