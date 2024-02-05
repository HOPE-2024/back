package com.hopeback.repository.medicine;

import com.hopeback.entity.medicine.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    // 입력받은 memberId(String, 유저아이디)와, documentId(String, 의약품id)로 일치하는 Member와 Medicine 엔티티를 찾아주는 코드
    // Member 엔티티에서 MemberId를 이용해 일치하는 데이터를 찾고,
    // Medicine 엔티티에서 DocumentId를 이용해 일치하는 데이터를 찾아서
    // Likes 엔티티에 일치하는 member와 medicine을 찾는다.
    Optional<Likes> findByMemberMemberIdAndMedicineDocumentId(String memberId, String documentId);
    List<Likes> findByMemberMemberId(String memberId);
}
