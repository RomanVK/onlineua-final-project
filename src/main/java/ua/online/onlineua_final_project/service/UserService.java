package ua.online.onlineua_final_project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.online.onlineua_final_project.entity.Book;
import ua.online.onlineua_final_project.entity.User;
import ua.online.onlineua_final_project.repository.BookRepository;
import ua.online.onlineua_final_project.repository.UserRepository;
import ua.online.onlineua_final_project.web.error.NoEntityException;

import javax.transaction.Transactional;


@Service
public class UserService {
    private UserRepository userRepository;
    private BookRepository bookRepository;

    @Autowired
    public UserService(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new NoEntityException("There is no user with email " + email));
    }

    @Transactional
    public void payPenalty(Long userId, Long bookId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NoEntityException("There is no user with id  " + userId));
        Book selectedBook = bookRepository.findById(bookId).orElseThrow(
                () -> new NoEntityException("There is no book with id  " + bookId));
        user.payPenalty(selectedBook);
    }
}

