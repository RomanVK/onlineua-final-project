package ua.online.onlineua_final_project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.online.onlineua_final_project.dto.BookDTO;
import ua.online.onlineua_final_project.entity.Book;
import ua.online.onlineua_final_project.repository.BookRepository;
import ua.online.onlineua_final_project.web.error.BookAlreadyExistException;

import java.util.List;

@Slf4j
@Service
public class BookService {
    BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
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

    private boolean isbnExists(final String isbn) {
        return bookRepository.findByIsbn(isbn).isPresent();
    }

}
