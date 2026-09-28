package com.example.services;

import com.example.exception.ResourceNotFoundException;
import com.example.models.Course;
import com.example.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    public Course updateCourse(Long id, Course updatedCourse) {
        Course existing = getCourseById(id);
        existing.setCourseName(updatedCourse.getCourseName());
        existing.setDescription(updatedCourse.getDescription());
        return courseRepository.save(existing);
    }

    public void deleteCourse(Long id) {
        Course existing = getCourseById(id);
        courseRepository.delete(existing);
    }

    public long countTotalCourses() {
        return courseRepository.count();
    }
}
