package ua.online.onlineua_final_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping(value = {"/", "/welcome"})
    public String mainPage(Model model) {
        return "welcome";
    }

    @RequestMapping(value = {"login"})
    public String loginForm(Model model){
        return "login";
    }

    @RequestMapping(value = {"registration_form"})
    public String registrationForm(Model model){
        return "registration_form";
    }

}