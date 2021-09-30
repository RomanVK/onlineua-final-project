package ua.online.onlineua_final_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.online.onlineua_final_project.entity.User;
import ua.online.onlineua_final_project.service.LibrarianService;

import java.util.List;

@Controller
@RequestMapping("librarian")
public class LibrarianController {

    LibrarianService librarianService;

    @Autowired
    public LibrarianController(LibrarianService librarianService) {
        this.librarianService = librarianService;
    }

    @GetMapping(value = {"librarianAccount"})
    public ModelAndView librarianAccount(Model model) {
        return new ModelAndView("librarian/librarianAccount");
    }

    @GetMapping(value = {"usersList"})
    public ModelAndView usersList(Model model) {
        ModelAndView mav = new ModelAndView("librarian/usersList");
        List<User> users = librarianService.getAllUsers();
        mav.addObject("users", users);
        return mav;
    }

}
