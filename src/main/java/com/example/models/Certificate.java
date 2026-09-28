package com.example.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "certificates")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String verificationCode;

    private LocalDate issueDate;

    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participant_id", nullable = false)
    private Participant participant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "certificate", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<VerificationRecord> verificationRecords = new ArrayList<>();

    public Certificate() {
    }

    public Certificate(Long id, String verificationCode, LocalDate issueDate, String status, Participant participant, Course course) {
        this.id = id;
        this.verificationCode = verificationCode;
        this.issueDate = issueDate;
        this.status = status;
        this.participant = participant;
        this.course = course;
    }

    @PrePersist
    public void generateDefaults() {
        if (verificationCode == null || verificationCode.isEmpty()) {
            verificationCode = UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 12)
                    .toUpperCase();
        }

        if (issueDate == null) {
            issueDate = LocalDate.now();
        }

        if (status == null || status.isEmpty()) {
            status = "ACTIVE";
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

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<VerificationRecord> getVerificationRecords() {
        return verificationRecords;
    }

    public void setVerificationRecords(List<VerificationRecord> verificationRecords) {
        this.verificationRecords = verificationRecords;
    }
}