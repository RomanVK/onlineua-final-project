package ua.online.onlineua_final_project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.online.onlineua_final_project.dto.SearchQueryDTO;
import ua.online.onlineua_final_project.entity.Book;
import ua.online.onlineua_final_project.service.BookService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("bookCatalog")
public class BookCatalogController {

    BookService bookService;

    @Autowired
    public BookCatalogController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = {"bookCatalog"})
    public ModelAndView bookCatalog(Model model) {
        ModelAndView mav = new ModelAndView("bookCatalog/bookCatalog");
        List<Book> books = bookService.getAllBooks();
        mav.addObject("books", books);
        mav.addObject("searchQueryByAuthor", new SearchQueryDTO());
        mav.addObject("searchQueryByBookName", new SearchQueryDTO());
        return mav;
    }

    @PostMapping(value = {"findBooksByAuthor"})
    public ModelAndView findBooksByAuthor(@ModelAttribute("searchQueryByAuthor") SearchQueryDTO searchQueryByAuthor) {
        ModelAndView mav = new ModelAndView("bookCatalog/bookCatalog");
        log.info("Try to find book by author. {}", searchQueryByAuthor);
        List<Book> books = new ArrayList<>();
        try {
            books = bookService.getAllBooksBySearchQueryByAuthor(searchQueryByAuthor.getContent());
            log.info("All Books by search query by author are successfully found. {}", searchQueryByAuthor);
            mav.addObject("message",
                    "All Books by search query by author are successfully found. " + searchQueryByAuthor);
        } catch (Exception ex) {
            log.info("Something went wrong. {}", ex.getMessage());
            mav.addObject("message",
                    "Something went wrong. " + ex.getMessage());
        }
        mav.addObject("books", books);
        mav.addObject("searchQueryByAuthor", new SearchQueryDTO());
        mav.addObject("searchQueryByBookName", new SearchQueryDTO());
        return mav;
    }

    @PostMapping(value = {"findBooksByBookName"})
    public ModelAndView findBooksByBookName(@ModelAttribute("searchQueryByBookName") SearchQueryDTO searchQueryByBookName) {
        ModelAndView mav = new ModelAndView("bookCatalog/bookCatalog");
        log.info("Try to find book by book name. {}", searchQueryByBookName);
        List<Book> books = new ArrayList<>();
        try {
            books = bookService.getAllBooksBySearchQueryByBookName(searchQueryByBookName.getContent());
            log.info("All Books by search query by book name are successfully found. {}", searchQueryByBookName);
            mav.addObject("message",
                    "All Books by search query by book name are successfully found. " + searchQueryByBookName);
        } catch (Exception ex) {
            log.info("Something went wrong. {}", ex.getMessage());
            mav.addObject("message",
                    "Something went wrong. " + ex.getMessage());
        }
        mav.addObject("books", books);
        mav.addObject("searchQueryByAuthor", new SearchQueryDTO());
        mav.addObject("searchQueryByBookName", new SearchQueryDTO());
        return mav;
    }

}
