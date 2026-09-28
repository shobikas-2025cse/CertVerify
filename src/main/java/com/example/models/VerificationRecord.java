package com.example.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_records")
public class VerificationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String verificationCode;

    private String result;

    private LocalDateTime verifiedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "certificate_id", nullable = true)
    private Certificate certificate;

    public VerificationRecord() {
    }

    public VerificationRecord(String verificationCode, String result, Certificate certificate) {
        this.verificationCode = verificationCode;
        this.result = result;
        this.certificate = certificate;
        this.verifiedAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (this.verifiedAt == null) {
            this.verifiedAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public LocalDateTime getVerificationTime() {
        return verifiedAt;
    }

    public void setVerificationTime(LocalDateTime verificationTime) {
        this.verifiedAt = verificationTime;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }
}