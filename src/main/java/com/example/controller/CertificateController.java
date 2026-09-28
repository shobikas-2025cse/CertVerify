package com.example.controller;

import com.example.dto.IssueCertificateRequest;
import com.example.models.Certificate;
import com.example.services.CertificateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/certificates")
@CrossOrigin(origins = "*")
public class CertificateController {

    private final CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @PostMapping
    public ResponseEntity<Certificate> issueCertificate(@Valid @RequestBody IssueCertificateRequest request) {
        Certificate issued = certificateService.issueCertificate(request);
        return new ResponseEntity<>(issued, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Certificate>> getAllCertificates() {
        List<Certificate> certificates = certificateService.getAllCertificates();
        return ResponseEntity.ok(certificates);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Certificate> getCertificateById(@PathVariable Long id) {
        Certificate certificate = certificateService.getCertificateById(id);
        return ResponseEntity.ok(certificate);
    }

    @GetMapping("/verify/{code}")
    public ResponseEntity<Map<String, Object>> verifyCertificate(@PathVariable String code) {
        Map<String, Object> result = certificateService.verifyCertificate(code);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}/revoke")
    public ResponseEntity<Certificate> revokeCertificate(@PathVariable Long id) {
        Certificate revoked = certificateService.revokeCertificate(id);
        return ResponseEntity.ok(revoked);
    }

    @GetMapping("/statistics/course/{courseId}")
    public ResponseEntity<Map<String, Object>> getCourseStatistics(@PathVariable Long courseId) {
        Map<String, Object> statistics = certificateService.getCourseStatistics(courseId);
        return ResponseEntity.ok(statistics);
    }
}
