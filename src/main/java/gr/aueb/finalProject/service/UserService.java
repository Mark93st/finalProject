package gr.aueb.finalProject.service;

import gr.aueb.finalProject.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();
    Optional<User> findById(Long id);
    User save(User user);
    void deleteById(Long id);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    User registerNewUser(User user);
}
