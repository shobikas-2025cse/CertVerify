package com.example.controller.web;

import com.example.dto.IssueCertificateRequest;
import com.example.exception.DuplicateResourceException;
import com.example.exception.ResourceNotFoundException;
import com.example.models.Certificate;
import com.example.models.Course;
import com.example.models.Participant;
import com.example.services.CertificateService;
import com.example.services.CourseService;
import com.example.services.ParticipantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/certificates")
public class CertificateWebController {

    private final CertificateService certificateService;
    private final ParticipantService participantService;
    private final CourseService courseService;

    @Autowired
    public CertificateWebController(CertificateService certificateService,
                                    ParticipantService participantService,
                                    CourseService courseService) {
        this.certificateService = certificateService;
        this.participantService = participantService;
        this.courseService = courseService;
    }

    @GetMapping
    public String listCertificates(Model model) {
        List<Certificate> certificates = certificateService.getAllCertificates();
        model.addAttribute("certificates", certificates);
        model.addAttribute("activePage", "certificates");
        return "certificates/list";
    }

    @GetMapping("/issue")
    public String issueCertificateForm(Model model) {
        if (!model.containsAttribute("request")) {
            model.addAttribute("request", new IssueCertificateRequest());
        }
        model.addAttribute("participants", participantService.getAllParticipants());
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("activePage", "certificates");
        return "certificates/issue";
    }

    @PostMapping("/issue")
    public String processIssueCertificate(@Valid @ModelAttribute("request") IssueCertificateRequest request,
                                          BindingResult result,
                                          RedirectAttributes redirectAttributes,
                                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("participants", participantService.getAllParticipants());
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("activePage", "certificates");
            return "certificates/issue";
        }

        try {
            Certificate issued = certificateService.issueCertificate(request);
            redirectAttributes.addFlashAttribute("successMessage", "Certificate issued successfully!");
            redirectAttributes.addFlashAttribute("issuedCertificate", issued);
            return "redirect:/certificates/issue";
        } catch (DuplicateResourceException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("participants", participantService.getAllParticipants());
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("activePage", "certificates");
            return "certificates/issue";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("participants", participantService.getAllParticipants());
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("activePage", "certificates");
            return "certificates/issue";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred while issuing certificate: " + e.getMessage());
            model.addAttribute("participants", participantService.getAllParticipants());
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("activePage", "certificates");
            return "certificates/issue";
        }
    }

    @GetMapping("/{id}")
    public String certificateDetails(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Certificate certificate = certificateService.getCertificateById(id);
            model.addAttribute("certificate", certificate);
            model.addAttribute("activePage", "certificates");
            return "certificates/details";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Certificate not found with id: " + id);
            return "redirect:/certificates";
        }
    }

    @PostMapping("/{id}/revoke")
    public String revokeCertificate(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            certificateService.revokeCertificate(id);
            redirectAttributes.addFlashAttribute("successMessage", "Certificate #" + id + " has been successfully revoked.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to revoke certificate: " + e.getMessage());
        }
        return "redirect:/certificates";
    }

    @GetMapping("/{id}/revoke")
    public String revokeCertificateGet(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return revokeCertificate(id, redirectAttributes);
    }
}
