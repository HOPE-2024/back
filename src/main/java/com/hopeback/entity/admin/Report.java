package com.hopeback.entity.admin;


import com.hopeback.constant.Authority;
import com.hopeback.entity.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "report")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Report {
    @Id
    @Column(name = "report_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long reportId;


    // 신고한 사람
    @OneToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    private Member reporter;

    // 신고 당한 사람
    @OneToOne
    @JoinColumn(name = "reported_id", nullable = false)
    private Member reported;

    //신고 분류
    @Column(name= "report_check",nullable = false)
    private String check;
  //신고 사유
  @Column(name= "report_reason",nullable = false)
    private String reason;
    //신고 일
    @Column(name= "report_date",nullable = false)
    private LocalDateTime date;
   //처리 상황(읽음,안 읽음)
   @Column(name= "report_status",nullable = false)
    private String status;
}
