package ua.online.onlineua_final_project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.online.onlineua_final_project.dto.BookDTO;
import ua.online.onlineua_final_project.dto.NoteDTO;
import ua.online.onlineua_final_project.entity.Book;
import ua.online.onlineua_final_project.entity.User;
import ua.online.onlineua_final_project.service.AdminService;
import ua.online.onlineua_final_project.service.BookService;
import ua.online.onlineua_final_project.service.LibrarianService;
import ua.online.onlineua_final_project.web.error.BookAlreadyExistException;
import ua.online.onlineua_final_project.web.error.UserAlreadyExistException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("admin")
public class AdminController {

    AdminService adminService;
    //TODO make separate method for preparing data for usersList
    LibrarianService librarianService;
    BookService bookService;

    @Autowired
    public AdminController(AdminService adminService, LibrarianService librarianService, BookService bookService) {
        this.adminService = adminService;
        this.librarianService = librarianService;
        this.bookService = bookService;
    }

    @GetMapping(value = {"adminAccount"})
    public ModelAndView adminAccount(Model model) {
        ModelAndView mav = new ModelAndView("admin/adminAccount");

        //make info about logged user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails loggedUserPrincipal = ((UserDetails) principal);
            log.info("Logged user is {}", loggedUserPrincipal);
            String emailLoggedUser = loggedUserPrincipal.getUsername();
            User loggedUser = adminService.getUserByEmail(emailLoggedUser);
            mav.addObject("loggedUser", loggedUser);
        } else {
            mav.addObject("message", "The Logged user is unknown");
        }
        return mav;
    }

    @GetMapping(value = {"librariansList"})
    public String librariansList(Model model) {
        List<User> librarians = adminService.getAllLibrarians();
        model.addAttribute("librarians", librarians);
        return "admin/librariansList";
    }

    @GetMapping(value = {"deleteLibrarian"})
    public ModelAndView deleteLibrarian(Model model, @RequestParam("id") long id) {
        log.info("Deleting librarian account with id: {}", id);
        ModelAndView mav;
        try {
            adminService.deleteLibrarian(id);
            log.info("Librarian with id {} was deleted", id);
            mav = new ModelAndView("admin/librariansList");
            mav.addObject("message", "Librarian with id " + id + " was deleted");
            List<User> librarians = adminService.getAllLibrarians();
            mav.addObject("librarians", librarians);
            return mav;
        } catch (Exception ex) {
            log.info("Something went a wrong way. Librarian with id {} wasn't deleted", id);
            mav = new ModelAndView("admin/librariansList");
            mav.addObject("message", "Something went a wrong way. Librarian with id " + id + " wasn't deleted");
            return mav;
        }
    }

    @GetMapping(value = {"showCreateLibrarian"})
    public String showCreateLibrarian(Model model) {
        model.addAttribute("librarian", new NoteDTO());
        return "admin/showCreateLibrarian";
    }

    //    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = {"createLibrarian"})
    public ModelAndView createLibrarian(@ModelAttribute("librarian") @Valid NoteDTO note,
                                        HttpServletRequest request, Errors errors, Model model) {
        log.info("Registering librarian account with information: {}", note);
        ModelAndView mav;
        try {
            User librarian = adminService.createLibrarian(note);
            log.info("Librarian with email {} was created", librarian.getEmail());
            mav = new ModelAndView("admin/librariansList");
            mav.addObject("message", "Librarian with email " + librarian.getEmail() + " was created");
            List<User> librarians = adminService.getAllLibrarians();
            mav.addObject("librarians", librarians);
            return mav;
        } catch (UserAlreadyExistException uaeEx) {
            log.info("An account for that username/email already exists.{}", note);
            mav = new ModelAndView("admin/showCreateLibrarian");
            mav.addObject("message", "An account for that username/email already exists.");
            return mav;
        }
    }

    //TODO make separated utility method for the same function from lock|unlockUserAccount
    @GetMapping(value = "lockUserAccount")
    public ModelAndView lockUser(Model model, @RequestParam("id") long id) {
        log.info("Try to lock user account with id: {}", id);
        ModelAndView mav = new ModelAndView("librarian/usersList");
        try {
            adminService.lockUser(id);
            log.info("User account with id {} was locked", id);
            mav.addObject("message", "User account with id " + id + " was locked");
            List<User> users = librarianService.getAllUsers();
            mav.addObject("users", users);
            return mav;
        } catch (Exception exception) {
            log.info("Something went a wrong way. User account with id {} wasn't locked", id);
            mav = new ModelAndView("librarian/usersList");
            mav.addObject("message",
                    "Something went a wrong way. User account with id " + id + " wasn't locked");
            return mav;
        }
    }

    @GetMapping(value = "unlockUserAccount")
    public ModelAndView unlockUser(Model model, @RequestParam("id") long id) {
        log.info("Try to unlock user account with id: {}", id);
        ModelAndView mav = new ModelAndView("librarian/usersList");
        try {
            adminService.unlockUser(id);
            log.info("User account with id {} was unlocked", id);
            mav.addObject("message", "User account with id " + id + " was unlocked");
            List<User> users = librarianService.getAllUsers();
            mav.addObject("users", users);
            return mav;
        } catch (Exception exception) {
            log.info("Something went a wrong way. User account with id {} wasn't unlocked", id);
            mav = new ModelAndView("librarian/usersList");
            mav.addObject("message",
                    "Something went a wrong way. User account with id " + id + " wasn't unlocked");
            return mav;
        }
    }

    @GetMapping(value = "showCreateBook")
    public String showCreateBook(Model model) {
        model.addAttribute("book", new BookDTO());
        return "admin/showCreateBook";
    }

    @PostMapping(value = {"createBook"})
    public ModelAndView createBook(@ModelAttribute("book") @Valid BookDTO bookDTO,
                                   HttpServletRequest request, Errors errors, Model model) {
        log.info("Try to create the book with following data: {}", bookDTO);
        ModelAndView mav;
        try {
            Book book = bookService.createBook(bookDTO);
            log.info("Book with {} was created", book);
            mav = new ModelAndView("bookCatalog/bookCatalog");
            mav.addObject("message", "The Book with following data  " + book + " was created");
            List<Book> books = bookService.getAllBooks();
            mav.addObject("books", books);
            return mav;
        } catch (BookAlreadyExistException baeEx) {
            log.info("The book with  ISBN: {} is already exists", bookDTO.getIsbn());
            mav = new ModelAndView("admin/showCreateBook");
            mav.addObject("message",
                    "The book with  ISBN: " + bookDTO.getIsbn() + " is already exists");
            return mav;
        }
    }

}
