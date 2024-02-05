package com.hopeback.repository.medicine;

import com.hopeback.entity.medicine.Medicine;
import com.hopeback.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    Optional<Medicine> findByDocumentId(String documentId);
}
