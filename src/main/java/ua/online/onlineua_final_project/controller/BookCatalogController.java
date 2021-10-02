package ua.online.onlineua_final_project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.online.onlineua_final_project.entity.Book;
import ua.online.onlineua_final_project.service.BookService;

import java.util.List;

@Slf4j
@Controller
public class BookCatalogController {

    BookService bookService;

    @Autowired
    public BookCatalogController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = {"bookCatalog"})
    public String bookCatalog(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "bookCatalog/bookCatalog";
    }


}
