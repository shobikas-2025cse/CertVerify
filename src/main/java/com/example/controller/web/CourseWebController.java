package com.example.controller.web;

import com.example.models.Course;
import com.example.repository.CertificateRepository;
import com.example.services.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/courses")
public class CourseWebController {

    private final CourseService courseService;
    private final CertificateRepository certificateRepository;

    @Autowired
    public CourseWebController(CourseService courseService, CertificateRepository certificateRepository) {
        this.courseService = courseService;
        this.certificateRepository = certificateRepository;
    }

    @GetMapping
    public String listCourses(Model model) {
        List<Course> courses = courseService.getAllCourses();
        Map<Long, Long> certCounts = new HashMap<>();
        for (Course course : courses) {
            certCounts.put(course.getId(), certificateRepository.countByCourseId(course.getId()));
        }
        model.addAttribute("courses", courses);
        model.addAttribute("certCounts", certCounts);
        model.addAttribute("activePage", "courses");
        return "courses/list";
    }

    @GetMapping("/new")
    public String newCourseForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("isEdit", false);
        model.addAttribute("activePage", "courses");
        return "courses/form";
    }

    @PostMapping
    public String saveCourse(@Valid @ModelAttribute("course") Course course,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", false);
            model.addAttribute("activePage", "courses");
            return "courses/form";
        }

        try {
            courseService.createCourse(course);
            redirectAttributes.addFlashAttribute("successMessage", "Course '" + course.getCourseName() + "' created successfully!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating course: " + e.getMessage());
            model.addAttribute("isEdit", false);
            model.addAttribute("activePage", "courses");
            return "courses/form";
        }
        return "redirect:/courses";
    }

    @GetMapping("/edit/{id}")
    public String editCourseForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Course course = courseService.getCourseById(id);
            model.addAttribute("course", course);
            model.addAttribute("isEdit", true);
            model.addAttribute("activePage", "courses");
            return "courses/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Course not found with id: " + id);
            return "redirect:/courses";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateCourse(@PathVariable Long id,
                               @Valid @ModelAttribute("course") Course course,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (result.hasErrors()) {
            course.setId(id);
            model.addAttribute("isEdit", true);
            model.addAttribute("activePage", "courses");
            return "courses/form";
        }

        try {
            courseService.updateCourse(id, course);
            redirectAttributes.addFlashAttribute("successMessage", "Course '" + course.getCourseName() + "' updated successfully!");
        } catch (Exception e) {
            course.setId(id);
            model.addAttribute("errorMessage", "Error updating course: " + e.getMessage());
            model.addAttribute("isEdit", true);
            model.addAttribute("activePage", "courses");
            return "courses/form";
        }
        return "redirect:/courses";
    }

    @PostMapping("/delete/{id}")
    public String deleteCoursePost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return deleteCourse(id, redirectAttributes);
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            courseService.deleteCourse(id);
            redirectAttributes.addFlashAttribute("successMessage", "Course deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete course. Certificates may have already been issued for this course.");
        }
        return "redirect:/courses";
    }
}
