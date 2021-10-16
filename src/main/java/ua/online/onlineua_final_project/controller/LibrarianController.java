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
import ua.online.onlineua_final_project.web.error.NoEntityException;

import java.util.List;

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
        //make info about logged user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails loggedUserPrincipal = ((UserDetails) principal);
            log.info("Logged user is {}", loggedUserPrincipal);
            String emailLoggedUser = loggedUserPrincipal.getUsername();
            User loggedUser = librarianService.getUserByEmail(emailLoggedUser);
            mav.addObject("loggedUser", loggedUser);
        } else {
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
            List<Book> userBooks = bookService.getAllUserBooks(id);
            mav.addObject("userBooks", userBooks);
            return mav;
        } catch (NoEntityException nex) {
            String message = nex.getMessage();
            log.info(message);
            mav = new ModelAndView("librarian/usersList");
            mav.addObject("message", message);
            return mav;
        }
    }

    @GetMapping(value = {"returnUserBook"})
    public ModelAndView returnUserBook(Model model,
                                       @RequestParam("userId") long userId,
                                       @RequestParam("bookId") long bookId) {
        ModelAndView mav = new ModelAndView("librarian/userAccount");
        try {
            //return user book
            bookService.returnUserBook(userId, bookId);
            //make user's data for page
            User selectedUser = librarianService.getUserById(userId);
            mav.addObject("selectedUser", selectedUser);
            //make user's book list for page
            List<Book> userBooks = bookService.getAllUserBooks(userId);
            mav.addObject("userBooks", userBooks);
            return mav;
        } catch (NoEntityException nex) {
            String message = nex.getMessage();
            log.info(message);
            mav.addObject("message", message);
            return mav;
        }
    }
}
