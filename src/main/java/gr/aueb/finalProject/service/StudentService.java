package gr.aueb.finalProject.service;

import gr.aueb.finalProject.model.Student;
import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<Student> findAll();
    Optional<Student> findById(Long id);
    Student save(Student student);
    void deleteById(Long id);
    List<Student> findByLastName(String lastName);
    Optional<Student> findByUserUsername(String username);
}
