package com.hopeback.repository;

import com.hopeback.entity.admin.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {


    List<Report> findByStatus(String statusList);
    List<Report> findByStatusNot(String status);
    List<Report> findByReporter_NameContainingOrReported_NameContaining(String reporterName, String reportedName);
    List<Report> findByReporter_MemberIdContainingOrReported_MemberIdContaining(String reporterName, String reportedName);
    List<Report> findByReporter_NickNameContainingOrReported_NickNameContaining(String reporterName, String reportedName);
}
