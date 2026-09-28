package com.example.repository;

import com.example.models.VerificationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerificationRecordRepository extends JpaRepository<VerificationRecord, Long> {
    List<VerificationRecord> findByVerificationCode(String verificationCode);
}
