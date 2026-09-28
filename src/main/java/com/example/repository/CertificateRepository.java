package com.example.repository;

import com.example.models.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    Optional<Certificate> findByVerificationCode(String verificationCode);
    List<Certificate> findByCourseId(Long courseId);
    long countByCourseId(Long courseId);
    boolean existsByParticipantIdAndCourseId(Long participantId, Long courseId);
    boolean existsByVerificationCode(String verificationCode);
    long countByStatus(String status);
    List<Certificate> findTop5ByOrderByIdDesc();
}