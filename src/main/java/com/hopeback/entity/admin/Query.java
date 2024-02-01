package com.hopeback.entity.admin;


import com.hopeback.entity.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "query")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Query {
    @Id
    @Column(name = "query_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long queryId;
    //질문자
    @OneToOne
    @JoinColumn(name = "questioner", nullable = false)
    private Member questioner;

    //질문 구분
    @Column(name= "division",nullable = false)
    private String division ;

    //질문 내용
    @Column(name= "substance",nullable = false)
    private String substance;

    //질문 이미지
    @Column(name= "img",nullable = false)
    private String queryImg;

    @OneToMany(mappedBy = "query", cascade = CascadeType.REMOVE)
    private List<Reply> reply;

    // Query 클래스에서 무한 루프 방지
    @Override
    public String toString() {
        return "Query{" +
                "queryId=" + queryId +
                ", questioner=" + (questioner != null ? questioner.getId() : null) + // 연관된 객체의 필요한 필드만 출력
                ", division='" + division + '\'' +
                ", substance='" + substance + '\'' +
                ", queryImg='" + queryImg + '\'' +
                '}';
    }
}
