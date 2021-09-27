package ua.online.onlineua_final_project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.online.onlineua_final_project.dto.NoteDTO;
import ua.online.onlineua_final_project.entity.RoleType;
import ua.online.onlineua_final_project.entity.User;
import ua.online.onlineua_final_project.repository.UserRepository;
import ua.online.onlineua_final_project.web.error.UserAlreadyExistException;

import java.util.List;

@Slf4j
@Service
public class AdminService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllLibrarians() {
        List<User> librarians = userRepository.findAllByRole(RoleType.LIBRARIAN);
        return librarians;
    }

    public void deleteLibrarian(long id) {
        userRepository.deleteById(id);
    }

    public User createLibrarian(NoteDTO note) throws UserAlreadyExistException {

        if (emailExists(note.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: "
                    + note.getEmail());
        }

        return userRepository.save(User.builder()
                .firstName(note.getFirstName())
                .lastName(note.getLastName())
                .email(note.getEmail())
                .role(RoleType.LIBRARIAN)
                .password(passwordEncoder.encode(note.getPassword()))
                .build());
    }

    private boolean emailExists(final String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElse(new User());
    }
}
