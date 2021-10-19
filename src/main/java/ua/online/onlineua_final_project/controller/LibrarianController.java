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
import ua.online.onlineua_final_project.service.BookService;
import ua.online.onlineua_final_project.service.LibrarianService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("librarian")
public class LibrarianController {

    LibrarianService librarianService;
    BookService bookService;

    @Autowired
    public LibrarianController(LibrarianService librarianService, BookService bookService) {
        this.librarianService = librarianService;
        this.bookService = bookService;
    }

    @GetMapping(value = {"librarianAccount"})
    public ModelAndView librarianAccount(Model model) {
        ModelAndView mav = new ModelAndView("librarian/librarianAccount");
        //make users list
        List<User> users = librarianService.getAllUsers();
        mav.addObject("users", users);
        //make info about logged user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails loggedUserPrincipal = ((UserDetails) principal);
            log.info("Logged user is {}", loggedUserPrincipal);
            String emailLoggedUser = loggedUserPrincipal.getUsername();
            User loggedUser = librarianService.getUserByEmail(emailLoggedUser);
            mav.addObject("loggedUser", loggedUser);
        } else {
            mav.addObject("loggedUser", new User());
            mav.addObject("message", "The Logged user is unknown");
        }
        return mav;
    }

    @GetMapping(value = {"usersList"})
    public ModelAndView usersList(Model model) {
        ModelAndView mav = new ModelAndView("librarian/usersList");
        List<User> users = librarianService.getAllUsers();
        mav.addObject("users", users);
        return mav;
    }

    @GetMapping(value = {"seeUserAccount"})
    public ModelAndView seeUserAccount(Model model, @RequestParam("id") long id) {
        log.info("Try to see user account with id: {}", id);
        ModelAndView mav;
        try {
            //make user's data for page
            User selectedUser = librarianService.getUserById(id);
            mav = new ModelAndView("librarian/userAccount");
            mav.addObject("selectedUser", selectedUser);
            //make user's book list for page
            Set<Book> booksInTheOrder = bookService.getUserBooksInOrder(id);
            Set<Book> booksOnTheSubscription = bookService.getUserBooksInSubscription(id);
            Set<Book> booksInTheReadingRoom = bookService.getUserBooksInReadingRoom(id);
            mav.addObject("booksInTheOrder", booksInTheOrder);
            mav.addObject("booksOnTheSubscription", booksOnTheSubscription);
            mav.addObject("booksInTheReadingRoom", booksInTheReadingRoom);
            return mav;
        } catch (RuntimeException rex) {
            String message = rex.getMessage();
            log.info(message);
            mav = new ModelAndView("librarian/usersList");
            mav.addObject("message", message);
            return mav;
        }
    }

    @GetMapping(value = {"giveOutTheBookToUserSubscription"})
    public ModelAndView giveOutTheBookToUserSubscription(Model model,
                                                         @RequestParam("userId") long userId,
                                                         @RequestParam("bookId") long bookId) {
        log.info("Try to give out book with id: {} to user subscription with id:{}", bookId, userId);
        ModelAndView mav = new ModelAndView("librarian/librarianAccount");
        try {
            //give out the book
            bookService.giveOutBookToTheSubscription(bookId, userId);
            //make users list for librarian
            List<User> users = librarianService.getAllUsers();
            mav.addObject("users", users);
            //make info about logged user
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails loggedUserPrincipal = ((UserDetails) principal);
                log.info("Logged user is {}", loggedUserPrincipal);
                String emailLoggedUser = loggedUserPrincipal.getUsername();
                User loggedUser = librarianService.getUserByEmail(emailLoggedUser);
                mav.addObject("loggedUser", loggedUser);
            } else {
                mav.addObject("loggedUser", new User());
                mav.addObject("message", "The Logged user is unknown");
            }
            return mav;
        } catch (RuntimeException rex) {
            String message = rex.getMessage();
            log.info(message);
            mav.addObject("loggedUser", new User());
            mav.addObject("users", new ArrayList<>());
            mav.addObject("message", message);
            return mav;
        }
    }

    @GetMapping(value = {"giveOutTheBookToReadingRoom"})
    public ModelAndView giveOutTheBookToReadingRoom(Model model,
                                                    @RequestParam("userId") long userId,
                                                    @RequestParam("bookId") long bookId) {
        log.info("Try to give out book with id: {} to the reading room, user id:{}", bookId, userId);
        ModelAndView mav = new ModelAndView("librarian/librarianAccount");
        try {
            //give out the book
            bookService.giveOutTheBookToTheReadingRoom(bookId, userId);
            //make users list for librarian
            List<User> users = librarianService.getAllUsers();
            mav.addObject("users", users);
            //make info about logged user
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails loggedUserPrincipal = ((UserDetails) principal);
                log.info("Logged user is {}", loggedUserPrincipal);
                String emailLoggedUser = loggedUserPrincipal.getUsername();
                User loggedUser = librarianService.getUserByEmail(emailLoggedUser);
                mav.addObject("loggedUser", loggedUser);
            } else {
                mav.addObject("loggedUser", new User());
                mav.addObject("message", "The Logged user is unknown");
            }
            return mav;
        } catch (RuntimeException rex) {
            String message = rex.getMessage();
            log.info(message);
            mav.addObject("loggedUser", new User());
            mav.addObject("users", new ArrayList<>());
            mav.addObject("message", message);
            return mav;
        }
    }

    @GetMapping(value = {"rejectTheOrder"})
    public ModelAndView rejectTheOrder(Model model,
                                       @RequestParam("userId") long userId,
                                       @RequestParam("bookId") long bookId) {
        log.info("Try to reject the order, book id:{}, user id:{}", bookId, userId);
        ModelAndView mav = new ModelAndView("librarian/librarianAccount");

        try {
            //give out the book
            bookService.rejectTheOrder(userId, bookId);
            mav.addObject("message",
                    "The Order for the book with id:" + bookId +
                            " from the user with id:" + userId + " was rejected");
            //make users list for librarian
            List<User> users = librarianService.getAllUsers();
            mav.addObject("users", users);
            //make info about logged user
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails loggedUserPrincipal = ((UserDetails) principal);
                log.info("Logged user is {}", loggedUserPrincipal);
                String emailLoggedUser = loggedUserPrincipal.getUsername();
                User loggedUser = librarianService.getUserByEmail(emailLoggedUser);
                mav.addObject("loggedUser", loggedUser);
            } else {
                mav.addObject("loggedUser", new User());
                mav.addObject("message", "The Logged user is unknown");
            }
            return mav;
        } catch (RuntimeException rex) {
            String message = rex.getMessage();
            log.info(message);
            mav.addObject("loggedUser", new User());
            mav.addObject("users", new ArrayList<>());
            mav.addObject("message", message);
            return mav;
        }
    }

    @GetMapping(value = {"returnUserBookFromSubscription"})
    public ModelAndView returnUserBookFromSubscription(Model model,
                                                       @RequestParam("userId") long userId,
                                                       @RequestParam("bookId") long bookId) {
        ModelAndView mav = new ModelAndView("librarian/userAccount");
        Set<Book> booksInTheOrder = new HashSet<>();
        Set<Book> booksOnTheSubscription = new HashSet<>();
        Set<Book> booksInTheReadingRoom = new HashSet<>();
        try {
            //return user book
            bookService.returnUserBookFromSubscription(userId, bookId);
            //make user's data for page
            User selectedUser = librarianService.getUserById(userId);
            mav.addObject("selectedUser", selectedUser);
            //make user's book list for page
            booksInTheOrder = bookService.getUserBooksInOrder(userId);
            booksOnTheSubscription = bookService.getUserBooksInSubscription(userId);
            booksInTheReadingRoom = bookService.getUserBooksInReadingRoom(userId);
            mav.addObject("booksInTheOrder", booksInTheOrder);
            mav.addObject("booksOnTheSubscription", booksOnTheSubscription);
            mav.addObject("booksInTheReadingRoom", booksInTheReadingRoom);
            mav.addObject("message",
                    "The Book with id:" + bookId + "was returned from user with id:" + userId);
            return mav;
        } catch (RuntimeException rex) {
            String message = rex.getMessage();
            log.info(message);
            mav.addObject("message", message);
            mav.addObject("booksInTheOrder", booksInTheOrder);
            mav.addObject("booksOnTheSubscription", booksOnTheSubscription);
            mav.addObject("booksInTheReadingRoom", booksInTheReadingRoom);
            return mav;
        }
    }

    @GetMapping(value = {"returnUserBookFromReadingRoom"})
    public ModelAndView returnUserBookFromReadingRoom(Model model,
                                                      @RequestParam("userId") long userId,
                                                      @RequestParam("bookId") long bookId) {
        ModelAndView mav = new ModelAndView("librarian/userAccount");
        Set<Book> booksInTheOrder = new HashSet<>();
        Set<Book> booksOnTheSubscription = new HashSet<>();
        Set<Book> booksInTheReadingRoom = new HashSet<>();
        try {
            //return user book
            bookService.returnUserBookFromReadingRoom(userId, bookId);
            //make user's data for page
            User selectedUser = librarianService.getUserById(userId);
            mav.addObject("selectedUser", selectedUser);
            //make user's book list for page
            booksInTheOrder = bookService.getUserBooksInOrder(userId);
            booksOnTheSubscription = bookService.getUserBooksInSubscription(userId);
            booksInTheReadingRoom = bookService.getUserBooksInReadingRoom(userId);
            mav.addObject("booksInTheOrder", booksInTheOrder);
            mav.addObject("booksOnTheSubscription", booksOnTheSubscription);
            mav.addObject("booksInTheReadingRoom", booksInTheReadingRoom);
            mav.addObject("message",
                    "The Book with id:" + bookId + "was returned from the user with id:" + userId);
            return mav;
        } catch (RuntimeException rex) {
            String message = rex.getMessage();
            log.info(message);
            mav.addObject("message", message);
            mav.addObject("selectedUser", new User());
            mav.addObject("booksInTheOrder", booksInTheOrder);
            mav.addObject("booksOnTheSubscription", booksOnTheSubscription);
            mav.addObject("booksInTheReadingRoom", booksInTheReadingRoom);
            return mav;
        }
    }
}
