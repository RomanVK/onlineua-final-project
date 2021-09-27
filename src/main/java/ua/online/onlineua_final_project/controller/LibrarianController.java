package ua.online.onlineua_final_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("librarian")
public class LibrarianController {

    @GetMapping(value = {"librarianAccount"})
    public ModelAndView librarianAccount(Model model) {
        return new ModelAndView("/librarian/librarianAccount");
    }

}
