package ua.online.onlineua_final_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import ua.online.onlineua_final_project.dto.NoteDTO;

@Controller
@RequestMapping("/")
public class PageController {

    @GetMapping(value = {"/", "/welcome"})
    public String mainPage(Model model) {
        return "welcome";
    }

    @GetMapping(value = {"login"})
    public String loginForm(Model model) {
        return "login";
    }

    @GetMapping(value = {"registration_form"})
    public String registrationForm(WebRequest request, Model model) {
        NoteDTO noteDTO = new NoteDTO();
        model.addAttribute("user", noteDTO);
        return "registration_form";
    }

    @GetMapping(value = {"catalog"})
    public ModelAndView catalog(Model model) {
        return new ModelAndView("/user/catalog");
    }

    @GetMapping(value = {"userAccount"})
    public ModelAndView userAccount(Model model) {
        return new ModelAndView("/user/userAccount");
    }

    @GetMapping(value = {"librarianAccount"})
    public ModelAndView librarianAccount(Model model) {
        return new ModelAndView("/librarian/librarianAccount");
    }

    @GetMapping(value = {"adminAccount"})
    public ModelAndView adminAccount(Model model) {
        return new ModelAndView("/admin/adminAccount");
    }

}