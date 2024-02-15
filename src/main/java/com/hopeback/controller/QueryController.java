package com.hopeback.controller;

import com.hopeback.dto.admin.QueryDto;
import com.hopeback.dto.admin.ReplyDto;
import com.hopeback.dto.admin.ReportDto;
import com.hopeback.dto.member.MemberDto;
import com.hopeback.dto.member.MemberResDto;
import com.hopeback.service.AdminService;
import com.hopeback.service.QueryService;
import com.hopeback.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/query")
@RequiredArgsConstructor
public class QueryController {
    private final AdminService adminService;
    private final QueryService queryService;


    // 1대1 문의 등록
    @PostMapping("/insertQuery")
    public ResponseEntity<Boolean>  insertQuery(@RequestBody QueryDto queryDto) {
        Boolean list= queryService.insertQuery(queryDto);
        return ResponseEntity.ok(list);
    }

    // 1대1 문의 조회
    @GetMapping("/selectQueryList")
    public ResponseEntity<List<QueryDto>> selectQueryList() {
        List<QueryDto> list = queryService.selectQueryList();
        return ResponseEntity.ok(list);
    }

    //내 문의 글 조회
    @GetMapping("/selectMyQuery")
    public ResponseEntity<List<QueryDto>> selectMyQuery() {
        List<QueryDto> list = queryService.selectMyQuery();
        return ResponseEntity.ok(list);
    }

    // 1대1 문의 하나 조회
    @GetMapping("/selectQuery")
    public ResponseEntity <QueryDto> selectQuery(@RequestParam Long id) {
        QueryDto list = queryService.selectQuery(id);
        return ResponseEntity.ok(list);
    }


    // 문의 글에 댓글 추가
    @PostMapping("/insertReply")
    public ResponseEntity <Boolean> insertReply(@RequestBody ReplyDto replyDto) {
        Boolean list = queryService.insertReply(replyDto);
        return ResponseEntity.ok(list);
    }

    // 신고 삭제
    @PostMapping("/deleteReply")
    public ResponseEntity<Boolean>  deleteReply(@RequestParam Long id) {
        Boolean list= queryService.deleteReply(id);
        return ResponseEntity.ok(list);
    }
    // 문의 글 삭제
    @PostMapping("/deleteQuery")
    public ResponseEntity<Boolean>  deleteQuery(@RequestParam Long id) {
        Boolean list= queryService.deleteQuery(id);
        return ResponseEntity.ok(list);
    }
    //댓글 수정
    @PostMapping("/updateReply")
    public ResponseEntity <Boolean> updateReply(@RequestBody ReplyDto replyDto) {
        Boolean list = queryService.updateReply(replyDto);
        return ResponseEntity.ok(list);
    }


    // 1대1 문의  수정
    @PostMapping("/updateQuery")
    public ResponseEntity<Boolean>  updateQuery(@RequestBody QueryDto queryDto) {
        Boolean list= queryService.updateQuery(queryDto);
        return ResponseEntity.ok(list);
    }



    //내 문의 글 조회
    @GetMapping("/oftenQuery")
    public ResponseEntity<List<QueryDto>> oftenQuery() {
        List<QueryDto> list = adminService.oftenQuery();
        return ResponseEntity.ok(list);
    }




















//-------------------------------------------------------------------------------------------------------------------------------------

}

