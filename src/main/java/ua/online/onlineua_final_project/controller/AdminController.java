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
import ua.online.onlineua_final_project.web.error.NoEntityException;
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
    public ModelAndView unlockUser(@RequestParam("id") long id) {
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

    @GetMapping(value = "usersList")
    public String usersList() {
        log.info("Redirecting to the librarian/usersList");
        return "librarian/usersList";
    }

    @RequestMapping(value = "createBook", method = RequestMethod.POST)
    public String createBook(@ModelAttribute("book") @Valid BookDTO bookDTO, Model model) {
        log.info("Try to create the book with following data: {}", bookDTO);
        try {
            Book book = bookService.createBook(bookDTO);
            log.info("Book with {} was created", book);
            String message = "The Book with following data  " + book + " was created";
            return "redirect:/bookCatalog/pageToRedirect/" + message + "?";
        } catch (BookAlreadyExistException baeEx) {
            log.info("The book with  ISBN: {} is already exists", bookDTO.getIsbn());
            model.addAttribute("message",
                    "The book with  ISBN: " + bookDTO.getIsbn() + " is already exists");
            return "admin/showCreateBook";
        }
    }

    @GetMapping(value = {"deleteBook"})
    public String deleteBook(@RequestParam("id") long id) {
        log.info("Try to delete Book with id: {}", id);
        String message;
        try {
            adminService.deleteBook(id);
            log.info("Book with id {} was deleted", id);
            message = "Book with id " + id + " was deleted";
        } catch (Exception ex) {
            log.info("Something went a wrong way. Book with id {} wasn't deleted", id);
            message = "Something went a wrong way. Book with id " + id + " wasn't deleted";
        }
        return "redirect:/bookCatalog/pageToRedirect/" + message + "?";
    }


    @GetMapping(value = {"showEditBook"})
    public ModelAndView showEditBook(@RequestParam("id") long id) {
        log.info("Try to find Book with id: {}", id);
        ModelAndView mav = new ModelAndView("admin/showEditBook");
        mav.addObject("book", new BookDTO());
        try {
            Book selectedBook = bookService.getBookById(id);
            log.info("Book with id {} was found", id);
            mav.addObject("message", "Book with id: " + id + " is editing");
            mav.addObject("selectedBook", selectedBook);
            mav.addObject("id", id);
        } catch (NoEntityException nex) {
            String message = nex.getMessage();
            log.info(message);
            mav.addObject("message", message);
        }
        return mav;
    }

    @PostMapping(value = {"editBook"})
    public String editBook(@ModelAttribute("book") @Valid BookDTO bookDTO, @RequestParam("id") long id, Model model) {
        log.info("Try to edit Book with id: {}", id);
        try {
            bookService.editBookById(id, bookDTO);
            log.info("Book with id {} was edited", id);
            String message = "Book with id: " + id + " was edited";
            return "redirect:/bookCatalog/pageToRedirect/" + message + "?";
        } catch (NoEntityException nex) {
            String message = nex.getMessage();
            log.info(message);
            model.addAttribute("message", message);
            return "admin/showCreateBook";
        }
    }


}
