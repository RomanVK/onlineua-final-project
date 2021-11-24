package ua.online.onlineua_final_project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ua.online.onlineua_final_project.dto.BookDTO;
import ua.online.onlineua_final_project.entity.Book;
import ua.online.onlineua_final_project.entity.User;
import ua.online.onlineua_final_project.entity.UserBookSubscription;
import ua.online.onlineua_final_project.repository.BookRepository;
import ua.online.onlineua_final_project.repository.UserRepository;
import ua.online.onlineua_final_project.web.error.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

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
        return bookRepository.findAll();
    }

    public long getTotalBooks() {
        log.info("Finding the total count of books from the dB.");
        return bookRepository.count();
    }

    public Page<Book> findPaginated(int pageNumber, int pageSize,
                                    String sortField, String sortDirection) {
        log.info("Fetching the paginated books from the dB.");
        final Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return bookRepository.findAll(pageable);
    }

    public void save(final Book book) {
        bookRepository.save(book);
    }

    @Transactional
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

    @Transactional
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

    public Set<Book> getUserBooksInOrder(Long userId) {
        return getUserById(userId).getBooksThatAreInUserOrder();
    }

    @Transactional
    public Set<UserBookSubscription> getUserBooksInSubscription(Long userId) {
        User selectedUser = getUserById(userId);
        selectedUser.updatePenalty();
        return selectedUser.getBooksThatAreInTheUserSubscription();
    }

    public Set<Book> getUserBooksInReadingRoom(Long userId) {
        return getUserById(userId).getBooksThatAreInTheReadingRoom();
    }

    @Transactional
    public Book orderBookById(Long bookId, Long userId) {
        Book orderedBook = getBookById(bookId);
        Set<User> bookUsersInOrder = orderedBook.getUsersWhoOrderedTheBook();

        User orderingUser = getUserById(userId);
        Set<Book> userBooksInOrder = orderingUser.getBooksThatAreInUserOrder();
        Set<UserBookSubscription> userBooksInSubscription = orderingUser.getBooksThatAreInTheUserSubscription();
        Set<Book> userBooksInReadingRoom = orderingUser.getBooksThatAreInTheReadingRoom();

        if (userBooksInOrder.contains(orderedBook))
            throw new UserAlreadyHasTheBook("You already ordered the book with id:" + bookId);

        if (userBooksInSubscription.contains(new UserBookSubscription(orderingUser, orderedBook)))
            throw new RuntimeException("You already have the book with id:" + bookId + " on the subscription");

        if (userBooksInReadingRoom.contains(orderedBook))
            throw new RuntimeException("You already have the book with id:" + bookId + " in the reading room");

        bookUsersInOrder.add(orderingUser);
        orderedBook.setUsersWhoOrderedTheBook(bookUsersInOrder);
        bookRepository.save(orderedBook);

        return getBookById(bookId);
    }

    //TODO make separate method for a duplication code
    @Transactional
    public void giveOutBookToTheSubscription(Long bookId, Long userId) {
        Book giveOutBook = getBookById(bookId);
        Set<User> bookUsersInOrder = giveOutBook.getUsersWhoOrderedTheBook();
        Set<UserBookSubscription> bookUsersInSubscription = giveOutBook.getUsersWhoHaveTheBookBySubscription();

        User orderingUser = getUserById(userId);
        Set<Book> userBooksInOrder = orderingUser.getBooksThatAreInUserOrder();
        Set<UserBookSubscription> userBooksInSubscription = orderingUser.getBooksThatAreInTheUserSubscription();
        Set<Book> userBooksInReadingRoom = orderingUser.getBooksThatAreInTheReadingRoom();

        if (!userBooksInOrder.contains(giveOutBook))
            throw new RuntimeException("The user doesn't have order by the book with id:" + bookId);

        if (userBooksInSubscription.contains(new UserBookSubscription(orderingUser, giveOutBook)))
            throw new RuntimeException("The user already have the book with id:" + bookId + " on the subscription");

        if (userBooksInReadingRoom.contains(giveOutBook))
            throw new RuntimeException("The user already have the book with id:" + bookId + " in the reading room");

        if (giveOutBook.getQuantity() <= 0)
            throw new RuntimeException("The quantity of the book with id:" + bookId + " is not enough");

        bookUsersInOrder.remove(orderingUser);
        giveOutBook.setUsersWhoOrderedTheBook(bookUsersInOrder);

        giveOutBook.addUser(orderingUser);

        giveOutBook.setQuantity(giveOutBook.getQuantity() - 1);

    }

    //TODO make separate method for a duplication code
    @Transactional
    public Book giveOutTheBookToTheReadingRoom(Long bookId, Long userId) {
        Book giveOutBook = getBookById(bookId);
        Set<User> bookUsersInOrder = giveOutBook.getUsersWhoOrderedTheBook();
        Set<User> bookUsersInReadingRoom = giveOutBook.getUsersWhoHaveTheBookInReadingRoom();

        User orderingUser = getUserById(userId);
        Set<Book> userBooksInOrder = orderingUser.getBooksThatAreInUserOrder();
        Set<UserBookSubscription> userBooksInSubscription = orderingUser.getBooksThatAreInTheUserSubscription();
        Set<Book> userBooksInReadingRoom = orderingUser.getBooksThatAreInTheReadingRoom();

        if (!userBooksInOrder.contains(giveOutBook))
            throw new RuntimeException("The user doesn't have order by the book with id:" + bookId);

        if (userBooksInSubscription.contains(new UserBookSubscription(orderingUser, giveOutBook)))
            throw new RuntimeException("The user already have the book with id:" + bookId + " on the subscription");

        if (userBooksInReadingRoom.contains(giveOutBook))
            throw new RuntimeException("The user already have the book with id:" + bookId + " in the reading room");

        if (giveOutBook.getQuantity() <= 0)
            throw new RuntimeException("The quantity of book with id:" + bookId + " is not enough");

        bookUsersInOrder.remove(orderingUser);
        giveOutBook.setUsersWhoOrderedTheBook(bookUsersInOrder);

        bookUsersInReadingRoom.add(orderingUser);
        giveOutBook.setUsersWhoHaveTheBookInReadingRoom(bookUsersInReadingRoom);

        giveOutBook.setQuantity(giveOutBook.getQuantity() - 1);

        bookRepository.save(giveOutBook);

        return getBookById(bookId);
    }

    @Transactional
    public void rejectTheOrder(Long userId, Long bookId) {
        Book orderedBook = getBookById(bookId);
        Set<User> bookUsersInOrder = orderedBook.getUsersWhoOrderedTheBook();

        User orderingUser = getUserById(userId);
        Set<Book> userBooksInOrder = orderingUser.getBooksThatAreInUserOrder();

        if (!userBooksInOrder.contains(orderedBook))
            throw new RuntimeException("The user doesn't have order by the book with id:" + bookId);

        bookUsersInOrder.remove(orderingUser);
        orderedBook.setUsersWhoOrderedTheBook(bookUsersInOrder);
        bookRepository.save(orderedBook);

    }


    @Transactional
    public void returnUserBookFromSubscription(Long userId, Long bookId) {
        Book returnedBook = getBookById(bookId);
        Set<UserBookSubscription> bookUsersInSubscription = returnedBook.getUsersWhoHaveTheBookBySubscription();

        User selectedUser = getUserById(userId);
        Set<UserBookSubscription> userBooksInSubscription = selectedUser.getBooksThatAreInTheUserSubscription();

        if (!userBooksInSubscription.contains(new UserBookSubscription(selectedUser, returnedBook)))
            throw new RuntimeException(
                    "User with id:" + userId + " doesn't have the book with id:" + bookId + "in his subscription");

        returnedBook.removeUser(selectedUser);
        returnedBook.setQuantity(returnedBook.getQuantity() + 1);
        bookRepository.save(returnedBook);

    }

    @Transactional
    public void returnUserBookFromReadingRoom(Long userId, Long bookId) {
        Book returnedBook = getBookById(bookId);
        Set<User> bookUsersInReadingRoom = returnedBook.getUsersWhoHaveTheBookInReadingRoom();

        User selectedUser = getUserById(userId);
        Set<Book> userBooksInReadingRoom = selectedUser.getBooksThatAreInTheReadingRoom();

        if (!userBooksInReadingRoom.contains(returnedBook))
            throw new RuntimeException(
                    "User with id:" + userId + " doesn't have the book with id:" + bookId + " in the reading room");

        bookUsersInReadingRoom.remove(selectedUser);
        returnedBook.setUsersWhoHaveTheBookInReadingRoom(bookUsersInReadingRoom);
        returnedBook.setQuantity(returnedBook.getQuantity() + 1);
        bookRepository.save(returnedBook);

    }

    public Page<Book> getAllBooksBySearchQueryByAuthorPaginated(String searchQueryByAuthor, int pageNumber, int pageSize,
                                                                String sortField, String sortDirection) {
        log.info("Finding the paginated books by author from the dB.");
        final Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return bookRepository.findAllByAuthorContains(searchQueryByAuthor, pageable);
    }

    public Page<Book> getAllBooksBySearchQueryByTitlePaginated(String searchQueryByTitle, int pageNumber, int pageSize,
                                                               String sortField, String sortDirection) {
        log.info("Finding the paginated books by title from the dB.");
        final Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
        return bookRepository.findAllByTitleContains(searchQueryByTitle, pageable);

    }

    private User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NoEntityException("There is no an user with that id:" + id));
    }

    private boolean isbnExists(final String isbn) {
        return bookRepository.findByIsbn(isbn).isPresent();
    }

}
