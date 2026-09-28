package com.example.dto;

import jakarta.validation.constraints.NotNull;

public class IssueCertificateRequest {

    @NotNull(message = "Participant ID is required")
    private Long participantId;

    @NotNull(message = "Course ID is required")
    private Long courseId;

    public IssueCertificateRequest() {
    }

    public IssueCertificateRequest(Long participantId, Long courseId) {
        this.participantId = participantId;
        this.courseId = courseId;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
