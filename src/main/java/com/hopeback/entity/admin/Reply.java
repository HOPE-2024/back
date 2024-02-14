package com.hopeback.entity.admin;


import com.hopeback.dto.admin.QueryDto;
import com.hopeback.entity.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "reply")
@Getter
@Setter
@NoArgsConstructor
public class Reply {
    @Id
    @Column(name = "reply_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long replyId;

    // 댓글단 사람
    @JoinColumn(name = "answerer", nullable = false)
    private String answerer;

    @JoinColumn(name = "answer", nullable = false)
    private String answer;

    //문의 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "query_id")
    private Query query;

    // Reply 클래스에서 무한 루프 방지
    @Override
    public String toString() {
        return "Reply{" +
                "replyId=" + replyId +
                ", answerer='" + answerer + '\'' +
                ", answer='" + answer + '\'' +
                ", query=" + (query != null ? query.getQueryId() : null) + // 연관된 객체의 필요한 필드만 출력
                '}';
    }
}
