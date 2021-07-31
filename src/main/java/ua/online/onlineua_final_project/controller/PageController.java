package ua.online.onlineua_final_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
    @RequestMapping(value = {"/", "login"})
    public String staticResource(Model model){
        return "login";
    }
}