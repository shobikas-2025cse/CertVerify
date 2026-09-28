package com.example.controller.web;

import com.example.services.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/verify")
public class VerificationWebController {

    private final CertificateService certificateService;

    @Autowired
    public VerificationWebController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping
    public String verifyPage(@RequestParam(value = "code", required = false) String code, Model model) {
        if (code != null && !code.trim().isEmpty()) {
            return "redirect:/verify/result?code=" + code.trim();
        }
        model.addAttribute("activePage", "verify");
        return "verification/verify";
    }

    @PostMapping
    public String handleVerifyPost(@RequestParam("code") String code, RedirectAttributes redirectAttributes) {
        if (code == null || code.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please enter a certificate verification code.");
            return "redirect:/verify";
        }
        return "redirect:/verify/result?code=" + code.trim();
    }

    @GetMapping("/result")
    public String verifyResult(@RequestParam(value = "code", required = false) String code,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (code == null || code.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please provide a valid verification code.");
            return "redirect:/verify";
        }

        String trimmedCode = code.trim();
        Map<String, Object> result = certificateService.verifyCertificate(trimmedCode);

        model.addAttribute("result", result);
        model.addAttribute("code", trimmedCode);
        model.addAttribute("activePage", "verify");
        return "verification/result";
    }
}
