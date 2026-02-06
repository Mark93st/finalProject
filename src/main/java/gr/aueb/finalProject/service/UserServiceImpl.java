package gr.aueb.finalProject.service;

import gr.aueb.finalProject.repository.UserRepository;
import gr.aueb.finalProject.dto.RegistrationDTO;
import gr.aueb.finalProject.model.Student;
import gr.aueb.finalProject.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional
    public User registerNewUser(RegistrationDTO registrationDTO) {
        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        User newUser = new User();
        newUser.setUsername(registrationDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        if (userRepository.count() == 0) {
            newUser.setRole("ROLE_ADMIN");
        } else {
            newUser.setRole("ROLE_STUDENT");
            Student newStudent = new Student();
            newStudent.setFirstName(registrationDTO.getFirstName());
            newStudent.setLastName(registrationDTO.getLastName());
            newStudent.setEmail(registrationDTO.getEmail());

            newUser.setStudent(newStudent);
            newStudent.setUser(newUser);
        }

        return userRepository.save(newUser);
    }
}
