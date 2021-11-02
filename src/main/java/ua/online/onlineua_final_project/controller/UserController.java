package ua.online.onlineua_final_project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ua.online.onlineua_final_project.entity.Book;
import ua.online.onlineua_final_project.entity.User;
import ua.online.onlineua_final_project.entity.UserBookSubscription;
import ua.online.onlineua_final_project.service.BookService;
import ua.online.onlineua_final_project.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("user")
public class UserController {

    BookService bookService;
    UserService userService;

    @Autowired
    public UserController(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping(value = {"userAccount"})
    public ModelAndView userAccount(Model model) {
        ModelAndView mav = new ModelAndView("user/userAccount");
        Set<Book> booksInTheOrder = new HashSet<>();
        Set<UserBookSubscription> booksOnTheSubscription = new HashSet<>();
        Set<Book> booksInTheReadingRoom = new HashSet<>();
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails loggedUserPrincipal = ((UserDetails) principal);
                String emailLoggedUser = loggedUserPrincipal.getUsername();
                User loggedUser = userService.getUserByEmail(emailLoggedUser);
                mav.addObject("loggedUser", loggedUser);
                Long userId = loggedUser.getId();
                booksInTheOrder = bookService.getUserBooksInOrder(userId);
                booksOnTheSubscription = bookService.getUserBooksInSubscription(userId);
                booksInTheReadingRoom = bookService.getUserBooksInReadingRoom(userId);
            } else {
                log.info("The Logged user is unknown");
                mav.addObject("message", "The Logged user is unknown");
            }
            mav.addObject("booksInTheOrder", booksInTheOrder);
            mav.addObject("booksOnTheSubscription", booksOnTheSubscription);
            mav.addObject("booksInTheReadingRoom", booksInTheReadingRoom);
            return mav;
        } catch (Exception ex) {
            log.info("Something went a wrong way");
            mav.addObject("message",
                    "Something went a wrong way.");
            mav.addObject("booksInTheOrder", booksInTheOrder);
            mav.addObject("booksOnTheSubscription", booksOnTheSubscription);
            mav.addObject("booksInTheReadingRoom", booksInTheReadingRoom);
            return mav;
        }
    }

    @GetMapping(value = {"orderBook"})
    public ModelAndView orderBook(Model model, @RequestParam("bookId") long bookId) {
        log.info("Try to order book with id: {}", bookId);
        ModelAndView mav = new ModelAndView("bookCatalog/bookCatalog");
        List<Book> books;
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails loggedUserPrincipal = ((UserDetails) principal);
                String emailLoggedUser = loggedUserPrincipal.getUsername();
                User loggedUser = userService.getUserByEmail(emailLoggedUser);
                Long userId = loggedUser.getId();
                bookService.orderBookById(bookId, userId);
                log.info("User with id {} ordered book with id {}", userId, bookId);
                mav.addObject("message",
                        "User with id " + userId + " ordered book with id " + bookId);
            } else {
                log.info("The Logged user is unknown");
                mav.addObject("message", "The Logged user is unknown");
            }

            books = bookService.getAllBooks();
            mav.addObject("books", books);
            return mav;
        } catch (Exception ex) {
            log.info(ex.getMessage() + "Book with id {} wasn't ordered", bookId);
            mav.addObject("message",
                    ex.getMessage() + "Book with id " + bookId + " wasn't ordered");
            books = bookService.getAllBooks();
            mav.addObject("books", books);
            return mav;
        }
    }
}
