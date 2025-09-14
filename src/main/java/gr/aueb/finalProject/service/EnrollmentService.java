package gr.aueb.finalProject.service;

import gr.aueb.finalProject.model.Enrollment;
import java.util.List;
import java.util.Optional;

public interface EnrollmentService {
    List<Enrollment> findAll();
    Optional<Enrollment> findById(Long id);
    Enrollment save(Enrollment enrollment);
    void deleteById(Long id);
    List<Enrollment> findByStudentId(Long studentId);
    List<Enrollment> findByCourseId(Long courseId);
    boolean existsByStudentAndCourse(Long studentId, Long courseId);
}
