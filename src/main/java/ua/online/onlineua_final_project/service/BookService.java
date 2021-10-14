package ua.online.onlineua_final_project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.online.onlineua_final_project.dto.BookDTO;
import ua.online.onlineua_final_project.entity.Book;
import ua.online.onlineua_final_project.entity.User;
import ua.online.onlineua_final_project.repository.BookRepository;
import ua.online.onlineua_final_project.repository.UserRepository;
import ua.online.onlineua_final_project.web.error.BookAlreadyExistException;
import ua.online.onlineua_final_project.web.error.NoEntityException;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class BookService {
    BookRepository bookRepository;
    UserRepository userRepository;

    @Autowired
    public BookService(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public List<Book> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books;
    }

    public Book createBook(BookDTO bookDTO) throws BookAlreadyExistException {

        if (isbnExists(bookDTO.getIsbn())) {
            throw new BookAlreadyExistException("There is a book with that isbn: "
                    + bookDTO.getIsbn());
        }

        return bookRepository.save(Book.builder()
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .publisher(bookDTO.getPublisher())
                .publishingDate(bookDTO.getPublishingDate())
                .isbn(bookDTO.getIsbn())
                .quantity(bookDTO.getQuantity())
                .build());
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new NoEntityException("There is no a book with that id:" + id));
    }

    public void editBookById(long id, BookDTO bookDTO) {
        Book selectedBook = getBookById(id);
        selectedBook.setAuthor(bookDTO.getAuthor());
        selectedBook.setIsbn(bookDTO.getIsbn());
        selectedBook.setPublisher(bookDTO.getPublisher());
        selectedBook.setQuantity(bookDTO.getQuantity());
        selectedBook.setPublishingDate(bookDTO.getPublishingDate());
        selectedBook.setTitle(bookDTO.getTitle());
        bookRepository.save(selectedBook);
    }

    public List<Book> getAllUserBooks(Long userId) {
        return getUserById(userId).getBooks();
    }

    @Transactional
    public Book orderBookById(Long bookId, Long userId) {
        Book orderedBook = getBookById(bookId);
        User orderingUser = getUserById(userId);

        List<User> bookUsers = orderedBook.getUsers();
        bookUsers.add(orderingUser);

        List<Book> userBooks = orderingUser.getBooks();
        userBooks.add(orderedBook);

        return getBookById(bookId);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NoEntityException("There is no an user with that id:" + id));
    }

    private boolean isbnExists(final String isbn) {
        return bookRepository.findByIsbn(isbn).isPresent();
    }

}
