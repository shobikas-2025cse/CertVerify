package com.example.services;

import com.example.dto.IssueCertificateRequest;
import com.example.exception.DuplicateResourceException;
import com.example.exception.ResourceNotFoundException;
import com.example.models.Certificate;
import com.example.models.Course;
import com.example.models.Participant;
import com.example.models.VerificationRecord;
import com.example.repository.CertificateRepository;
import com.example.repository.CourseRepository;
import com.example.repository.ParticipantRepository;
import com.example.repository.VerificationRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final ParticipantRepository participantRepository;
    private final CourseRepository courseRepository;
    private final VerificationRecordRepository verificationRecordRepository;

    @Autowired
    public CertificateService(CertificateRepository certificateRepository,
                              ParticipantRepository participantRepository,
                              CourseRepository courseRepository,
                              VerificationRecordRepository verificationRecordRepository) {
        this.certificateRepository = certificateRepository;
        this.participantRepository = participantRepository;
        this.courseRepository = courseRepository;
        this.verificationRecordRepository = verificationRecordRepository;
    }

    public Certificate issueCertificate(IssueCertificateRequest request) {
        Participant participant = participantRepository.findById(request.getParticipantId())
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found with id: " + request.getParticipantId()));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        if (certificateRepository.existsByParticipantIdAndCourseId(request.getParticipantId(), request.getCourseId())) {
            throw new DuplicateResourceException("Certificate already exists for this participant and course");
        }

        String verificationCode;
        do {
            verificationCode = UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
        } while (certificateRepository.existsByVerificationCode(verificationCode));

        Certificate certificate = new Certificate();
        certificate.setParticipant(participant);
        certificate.setCourse(course);
        certificate.setVerificationCode(verificationCode);
        certificate.setIssueDate(LocalDate.now());
        certificate.setStatus("ACTIVE");

        return certificateRepository.save(certificate);
    }

    public List<Certificate> getAllCertificates() {
        return certificateRepository.findAll();
    }

    public Certificate getCertificateById(Long id) {
        return certificateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found with id: " + id));
    }

    public Map<String, Object> verifyCertificate(String verificationCode) {
        Optional<Certificate> optionalCert = certificateRepository.findByVerificationCode(verificationCode);

        if (optionalCert.isEmpty()) {
            VerificationRecord record = new VerificationRecord();
            record.setVerificationCode(verificationCode);
            record.setVerificationTime(LocalDateTime.now());
            record.setResult("INVALID");
            record.setCertificate(null);
            verificationRecordRepository.save(record);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("valid", false);
            response.put("message", "Certificate is invalid");
            return response;
        }

        Certificate certificate = optionalCert.get();

        if ("REVOKED".equalsIgnoreCase(certificate.getStatus())) {
            VerificationRecord record = new VerificationRecord();
            record.setVerificationCode(verificationCode);
            record.setVerificationTime(LocalDateTime.now());
            record.setResult("INVALID - REVOKED");
            record.setCertificate(certificate);
            verificationRecordRepository.save(record);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("valid", false);
            response.put("message", "Certificate has been revoked");
            response.put("certificateId", certificate.getId());
            response.put("holderName", certificate.getParticipant().getName());
            response.put("course", certificate.getCourse().getCourseName());
            response.put("issueDate", certificate.getIssueDate().toString());
            response.put("status", "REVOKED");
            response.put("verificationCode", verificationCode);
            return response;
        }

        // Active certificate
        VerificationRecord record = new VerificationRecord();
        record.setVerificationCode(verificationCode);
        record.setVerificationTime(LocalDateTime.now());
        record.setResult("VALID");
        record.setCertificate(certificate);
        verificationRecordRepository.save(record);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("valid", true);
        response.put("message", "Certificate is valid");
        response.put("certificateId", certificate.getId());
        response.put("holderName", certificate.getParticipant().getName());
        response.put("course", certificate.getCourse().getCourseName());
        response.put("issueDate", certificate.getIssueDate().toString());
        response.put("status", certificate.getStatus());
        response.put("verificationCode", verificationCode);
        return response;
    }

    public Certificate revokeCertificate(Long id) {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found with id: " + id));

        certificate.setStatus("REVOKED");
        return certificateRepository.save(certificate);
    }

    public Map<String, Object> getCourseStatistics(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        long totalCertificates = certificateRepository.countByCourseId(courseId);

        Map<String, Object> statistics = new LinkedHashMap<>();
        statistics.put("courseId", course.getId());
        statistics.put("courseName", course.getCourseName());
        statistics.put("totalCertificates", totalCertificates);
        return statistics;
    }

    public long countTotalCertificates() {
        return certificateRepository.count();
    }

    public long countActiveCertificates() {
        return certificateRepository.countByStatus("ACTIVE");
    }

    public long countRevokedCertificates() {
        return certificateRepository.countByStatus("REVOKED");
    }

    public List<Certificate> getRecentCertificates() {
        return certificateRepository.findTop5ByOrderByIdDesc();
    }
}
