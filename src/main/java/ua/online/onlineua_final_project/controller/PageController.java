package ua.online.onlineua_final_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import ua.online.onlineua_final_project.dto.NoteDTO;
import ua.online.onlineua_final_project.entity.User;

import java.util.ArrayList;
import java.util.List;

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

}