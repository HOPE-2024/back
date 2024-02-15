package com.hopeback.controller.medicine;

import com.hopeback.dto.Medicine.LikesDto;
import com.hopeback.service.medicine.LikesService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/medicine/likes")
@RequiredArgsConstructor
public class LikesController {
    private final LikesService likesService;

    // 한 회원의 특정 의약품 즐겨찾기 여부 조회
    @PostMapping("/getLikes")
    public ResponseEntity<Boolean> getLikes (@RequestBody LikesDto likesDto) {
        boolean isTrue = likesService.getLikes(likesDto);
        return ResponseEntity.ok(isTrue);
    }

    // 한 회원의 의약품 즐겨찾기 목록
    @GetMapping("/getAllLikes")
    public ResponseEntity<List<LikesDto>> getAllLikes (@RequestParam String memberId) {
        List<LikesDto> likesDtoList = likesService.getAllLikes(memberId);
        return ResponseEntity.ok(likesDtoList);
    }
    // 즐겨찾기에 추가
    @PostMapping("/add")
    public ResponseEntity<Boolean> likesAdd (@RequestBody LikesDto likesDto) {
        System.out.println("member : " + likesDto.getMemberId());
        System.out.println("documentId : " + likesDto.getDocumentId());
        boolean isTrue = likesService.likesAdd(likesDto);
        return ResponseEntity.ok(isTrue);
    }

    // 즐겨찾기에 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> likesDelete (@RequestParam("memberId") String memberId,
                                                @RequestParam("documentId") String documentId) {
        System.out.println("member : " + memberId);
        System.out.println("documentId : " + documentId);
        boolean isTrue = likesService.likesDelete(memberId, documentId);
        return ResponseEntity.ok(isTrue);
    }
}
