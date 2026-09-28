package com.example.controller.web;

import com.example.services.CertificateService;
import com.example.services.CourseService;
import com.example.services.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardWebController {

    private final ParticipantService participantService;
    private final CourseService courseService;
    private final CertificateService certificateService;

    @Autowired
    public DashboardWebController(ParticipantService participantService,
                                  CourseService courseService,
                                  CertificateService certificateService) {
        this.participantService = participantService;
        this.courseService = courseService;
        this.certificateService = certificateService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalParticipants", participantService.countTotalParticipants());
        model.addAttribute("totalCourses", courseService.countTotalCourses());
        model.addAttribute("totalCertificates", certificateService.countTotalCertificates());
        model.addAttribute("activeCertificates", certificateService.countActiveCertificates());
        model.addAttribute("revokedCertificates", certificateService.countRevokedCertificates());
        model.addAttribute("recentCertificates", certificateService.getRecentCertificates());
        model.addAttribute("activePage", "dashboard");
        return "dashboard";
    }
}
