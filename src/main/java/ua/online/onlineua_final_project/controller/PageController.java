package ua.online.onlineua_final_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PageController {

    @GetMapping(value = {"/", "/welcome"})
    public String mainPage(Model model) {
        return "welcome";
    }

    @GetMapping(value = {"login"})
    public String loginForm(Model model){
        return "login";
    }

    @GetMapping(value = {"registration_form"})
    public String registrationForm(Model model){
        return "registration_form";
    }

    @GetMapping(value = {"catalog"})
    public String catalog(Model model){
        return "catalog";
    }
}