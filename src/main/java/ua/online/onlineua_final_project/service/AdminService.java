package ua.online.onlineua_final_project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.online.onlineua_final_project.dto.NoteDTO;
import ua.online.onlineua_final_project.entity.RoleType;
import ua.online.onlineua_final_project.entity.User;
import ua.online.onlineua_final_project.repository.BookRepository;
import ua.online.onlineua_final_project.repository.UserRepository;
import ua.online.onlineua_final_project.web.error.NoEntityException;
import ua.online.onlineua_final_project.web.error.UserAlreadyExistException;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class AdminService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private BookRepository bookRepository;

    @Autowired
    public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.bookRepository = bookRepository;
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
                .nonLocked(true)
                .build());
    }

    @Transactional
    public void lockUser(long id) {
        User lockUser = userRepository.findById(id).
                orElseThrow(() -> new NoEntityException("There is no user with id:" + id));
        lockUser.setNonLocked(false);
        userRepository.save(lockUser);
    }

    @Transactional
    public void unlockUser(long id) {
        User unlockUser = userRepository.findById(id).
                orElseThrow(() -> new NoEntityException("There is no user with id:" + id));
        unlockUser.setNonLocked(true);
        userRepository.save(unlockUser);
    }

    public void deleteBook(long id) {
        bookRepository.deleteById(id);
    }

    private boolean emailExists(final String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new NoEntityException("There is no user with email " + email));
    }
}
