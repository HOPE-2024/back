package com.hopeback.service.medicine;

import com.hopeback.dto.Medicine.LikesDto;
import com.hopeback.entity.medicine.Likes;
import com.hopeback.entity.medicine.Medicine;
import com.hopeback.entity.member.Member;
import com.hopeback.repository.MemberRepository;
import com.hopeback.repository.medicine.LikesRepository;
import com.hopeback.repository.medicine.MedicineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LikesService {
    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;
    private final MedicineRepository medicineRepository;

    // 한 회원의 특정 의약품 즐겨찾기 여부 조회
    public Boolean getLikes(LikesDto likesDto) {
            Optional<Likes> likesOptional = likesRepository.findByMemberMemberIdAndMedicineDocumentId(likesDto.getMemberId(), likesDto.getDocumentId());
            // 즐겨찾기가 존재하면 true 반환, 그렇지 않으면 false 반환
            return likesOptional.isPresent();
    }

    // 한 회원의 의약품 즐겨찾기 목록
    public List<LikesDto> getAllLikes(String memberId) {
        List<Likes> likesList = likesRepository.findByMemberMemberId(memberId);
        List<LikesDto> likesDtoList = new ArrayList<>();
        for (Likes likes : likesList) {
            LikesDto likesDto = new LikesDto();
            likesDto.setId(likes.getId());
            likesDto.setMemberId(likes.getMember().getMemberId());
            likesDto.setDocumentId(likes.getMedicine().getDocumentId());

            likesDtoList.add(likesDto);
        }
        return likesDtoList;
    }

    // 즐겨찾기 추가
    public Boolean likesAdd(LikesDto likesDto) {
        try {
            Member member = memberRepository.findByMemberId(likesDto.getMemberId()).orElseThrow(
                    ()-> new RuntimeException("해당 회원이 존재하지 않습니다.")
            );
            Medicine medicine = medicineRepository.findByDocumentId(likesDto.getDocumentId()).orElseThrow(
                    () -> new RuntimeException("해당 의약품이 존재하지 않습니다.")
            );
            System.out.println("member, medicine : " + member + medicine);
            Likes likes = new Likes();
            likes.setMember(member);
            likes.setMedicine(medicine);
            likesRepository.save(likes);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // 즐겨찾기 삭제
    // 두개의 값이 일치하는 데이터를 삭제해야 함.
    public Boolean likesDelete(String memberId, String documentId) {
        try {
            Likes likes = likesRepository.findByMemberMemberIdAndMedicineDocumentId(memberId, documentId).orElseThrow(
                    () -> new RuntimeException("해당 즐겨찾기를 찾을 수 없습니다.")
            );
            likesRepository.delete(likes);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
