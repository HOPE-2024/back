package com.hopeback.entity.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "naver")
@Getter @Setter
@NoArgsConstructor
public class Naver {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    private String nickName;
    private String email;
    private String name;

    @Builder
    public Naver(Long id, String nickName, String email, String name) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
        this.name = name;
    }
}
