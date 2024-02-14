package com.hopeback.repository;

import com.hopeback.entity.admin.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Page<Report> findAll(Pageable pageable);   // 전체 조회
    List<Report> findAllByStatus(String status, Pageable pageable);
    List<Report> findAllByStatusNot(String status, Pageable pageable);
    int countByStatus(String status);
    int countByStatusNot(String status);
    List<Report> findByStatus(String statusList);
    List<Report> findByStatusNot(String status);
    List<Report> findByReporter_NameContainingOrReported_NameContaining(String reporterName, String reportedName);
    List<Report> findByReporter_MemberIdContainingOrReported_MemberIdContaining(String reporterName, String reportedName);
    List<Report> findByReporter_NickNameContainingOrReported_NickNameContaining(String reporterName, String reportedName);
}
