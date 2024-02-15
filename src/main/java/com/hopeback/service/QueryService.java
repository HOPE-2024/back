package com.hopeback.service;

import com.hopeback.constant.Authority;
import com.hopeback.dto.admin.QueryDto;
import com.hopeback.dto.admin.ReplyDto;
import com.hopeback.dto.admin.ReportDto;
import com.hopeback.entity.admin.Query;
import com.hopeback.entity.admin.Reply;
import com.hopeback.entity.admin.Report;
import com.hopeback.entity.member.Member;
import com.hopeback.repository.MemberRepository;
import com.hopeback.repository.QueryRepository;
import com.hopeback.repository.ReplyRepository;
import com.hopeback.repository.ReportRepository;
import com.hopeback.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryService {
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;
    private final QueryRepository queryRepository;
    private final ReplyRepository replyRepository;
    private final ModelMapper modelMapper;

    @Transactional
    //1대1 문의 등록
    public Boolean  insertQuery(QueryDto dto){
        try{
            String memberId = SecurityUtil.getCurrentMemberId();
            Member member = memberRepository.findByMemberId(memberId).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재하지 않습니다."));
            Query query = new Query();
            query.setQuestioner(member);
            query.setTitle(dto.getTitle());
            query.setDivision(dto.getDivision());
            query.setSubstance(dto.getSubstance());
            query.setQueryImg(dto.getQueryImg());
            if (member.getAuthority() == Authority.ADMIN) {
                log.warn("관리자 입니다.");
                query.setOften("FAQ");
                query.setStatus("답변 완료");

            } else {
                log.warn("회원 입니다.");
                query.setOften("1대1 문의 ");
                query.setStatus("답변 전");
            }

            queryRepository.save(query);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    //1대1 문의 전부 출력
    public List<QueryDto> selectQueryList() {
        List<Query> reports = queryRepository.findAll();
        return reports.stream()
                .map(report -> {
                    QueryDto queryDto = modelMapper.map(report, QueryDto.class);
                    return queryDto;
                })
                .collect(Collectors.toList());
    }

    //내 문의 글 조회
    public List<QueryDto> selectMyQuery() {
        String memberId = SecurityUtil.getCurrentMemberId();
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(
                () -> new RuntimeException("해당 회원이 존재하지 않습니다."));
        List<Query> queries = queryRepository.findByQuestionerNickNameAndOftenNot(member.getNickName(), "자주 하는 질문");

        return queries.stream()
                .map(query -> modelMapper.map(query, QueryDto.class))
                .collect(Collectors.toList());
    }

    //1대1 문의 하나 출력
    public QueryDto selectQuery( Long id) {
        Query report = queryRepository.findById(id).orElseThrow(
                () -> new RuntimeException("해당 번호의 문의는 없습니다."));

        return modelMapper.map(report, QueryDto.class);
    }

    @Transactional
    //댓글 등록
    public Boolean  insertReply(ReplyDto dto){
        try{
            String memberId = SecurityUtil.getCurrentMemberId();
            Member member = memberRepository.findByMemberId(memberId).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재하지 않습니다."));
            Query query = queryRepository.findById(dto.getQueryId()).orElseThrow(
                    () -> new RuntimeException("해당 글이 존재하지 않습니다."));
            Reply reply = new Reply();
            reply.setAnswer(dto.getAnswer());
            reply.setAnswerer(member.getNickName());
            reply.setQuery(query);
            replyRepository.save(reply);
            if (member.getAuthority() == Authority.ADMIN) {
                log.warn("관리자 입니다.");
                query.setStatus("답변 완료");
                queryRepository.save(query);
            } else {
                log.warn("회원 입니다.");
            }

            return true;
        }catch (Exception e){
            log.warn(String.valueOf(e));
            return false;
        }
    }

    @Transactional
    //문의글 삭제
    public Boolean deleteQuery(Long id){
        Query query = queryRepository.findById(id).orElseThrow(
                () -> new RuntimeException("신고 내역이 없습니다."));
        if(query !=null){
            queryRepository.deleteById(id);
            return true;
        }else {
            return false;
        }
    }


    @Transactional
    //댓글 수정
    public Boolean updateReply(ReplyDto replyDto) {
        Reply reply = replyRepository.findById(replyDto.getId()).orElseThrow(
                () -> new RuntimeException("댓글 내역이 없습니다."));
        if (reply != null) {
            reply.setAnswer(replyDto.getAnswer());
            return true;
        } else {
            return false;
        }
    }



    @Transactional
    //댓글 삭제
    public Boolean deleteReply(Long id){
        Reply reply = replyRepository.findById(id).orElseThrow(
                () -> new RuntimeException("댓글 내역이 없습니다."));
        if(reply !=null){
            replyRepository.deleteById(id);
            return true;
        }else {
            return false;
        }
    }




    @Transactional
    // 1대1 문의  수정
    public Boolean  updateQuery(QueryDto dto){
        try{
            Query query  = queryRepository.findById(dto.getId()).orElseThrow(
                    () -> new RuntimeException("해당 글이 존재하지 않습니다."));
            query.setDivision(dto.getDivision());
            query.setSubstance(dto.getSubstance());
            query.setTitle(dto.getTitle());
            if(dto.getQueryImg() != null){
                query.setQueryImg(dto.getQueryImg());
            }
            queryRepository.save(query);
            return true;
        }catch (Exception e){
            return false;
        }
    }





}