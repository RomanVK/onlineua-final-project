package ua.online.onlineua_final_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("librarian")
public class LibrarianController {

    @GetMapping("orders")
    public String orders(Model model){
        return "librarian/orders";
    }

}
