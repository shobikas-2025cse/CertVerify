package com.example.controller.web;

import com.example.models.Participant;
import com.example.services.ParticipantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/participants")
public class ParticipantWebController {

    private final ParticipantService participantService;

    @Autowired
    public ParticipantWebController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping
    public String listParticipants(Model model) {
        model.addAttribute("participants", participantService.getAllParticipants());
        model.addAttribute("activePage", "participants");
        return "participants/list";
    }

    @GetMapping("/new")
    public String newParticipantForm(Model model) {
        model.addAttribute("participant", new Participant());
        model.addAttribute("isEdit", false);
        model.addAttribute("activePage", "participants");
        return "participants/form";
    }

    @PostMapping
    public String saveParticipant(@Valid @ModelAttribute("participant") Participant participant,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", false);
            model.addAttribute("activePage", "participants");
            return "participants/form";
        }

        try {
            participantService.createParticipant(participant);
            redirectAttributes.addFlashAttribute("successMessage", "Participant '" + participant.getName() + "' created successfully!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating participant: " + e.getMessage());
            model.addAttribute("isEdit", false);
            model.addAttribute("activePage", "participants");
            return "participants/form";
        }
        return "redirect:/participants";
    }

    @GetMapping("/edit/{id}")
    public String editParticipantForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Participant participant = participantService.getParticipantById(id);
            model.addAttribute("participant", participant);
            model.addAttribute("isEdit", true);
            model.addAttribute("activePage", "participants");
            return "participants/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Participant not found with id: " + id);
            return "redirect:/participants";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateParticipant(@PathVariable Long id,
                                    @Valid @ModelAttribute("participant") Participant participant,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        if (result.hasErrors()) {
            participant.setId(id);
            model.addAttribute("isEdit", true);
            model.addAttribute("activePage", "participants");
            return "participants/form";
        }

        try {
            participantService.updateParticipant(id, participant);
            redirectAttributes.addFlashAttribute("successMessage", "Participant '" + participant.getName() + "' updated successfully!");
        } catch (Exception e) {
            participant.setId(id);
            model.addAttribute("errorMessage", "Error updating participant: " + e.getMessage());
            model.addAttribute("isEdit", true);
            model.addAttribute("activePage", "participants");
            return "participants/form";
        }
        return "redirect:/participants";
    }

    @PostMapping("/delete/{id}")
    public String deleteParticipantPost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return deleteParticipant(id, redirectAttributes);
    }

    @GetMapping("/delete/{id}")
    public String deleteParticipant(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            participantService.deleteParticipant(id);
            redirectAttributes.addFlashAttribute("successMessage", "Participant deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete participant. They may have active certificates issued.");
        }
        return "redirect:/participants";
    }
}
