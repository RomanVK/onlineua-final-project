package ua.online.onlineua_final_project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.online.onlineua_final_project.dto.SearchQueryDTO;
import ua.online.onlineua_final_project.entity.Book;
import ua.online.onlineua_final_project.service.BookService;

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

    //took https://examples.javacodegeeks.com/spring-boot-sorting-with-thymeleaf-tutorial/
    @GetMapping(value = {"/pageToRedirect/{message}"})
    public String pageToRedirect(@PathVariable(name = "message") String message, RedirectAttributes attributes) {
        log.info("Redirecting to bookCatalog/page with message : " + message);
        attributes.addFlashAttribute("message", message);
        return "redirect:/bookCatalog/page/1?sort-field=id&sort-dir=asc&search-request-by-author=&search-request-by-title=";
    }

    @GetMapping(value = {"/page"})
    public String bookCatalog() {
        log.info("Redirecting to bookCatalog/page");
        return "redirect:/bookCatalog/page/1?sort-field=id&sort-dir=asc&search-request-by-author=&search-request-by-title=";
    }

    @GetMapping(value = "/page/{page-number}")
    public String bookCatalogPaginated(@PathVariable(name = "page-number") int pageNo,
                                       @RequestParam(name = "sort-field") String sortField,
                                       @RequestParam(name = "sort-dir") String sortDir,
                                       @RequestParam(name = "search-request-by-author") String searchRequestByAuthor,
                                       @RequestParam(name = "search-request-by-title") String searchRequestByTitle,
                                       Model model) {
        log.info("Getting the books in a paginated way for page-number = {}, sort-field = {}, and "
                + "sort-direction = {}.", pageNo, sortField, sortDir);
        int pageSize = 15;

        try {
            Page<Book> page = bookService.findPaginated(pageNo, pageSize, sortField, sortDir);

            if (!searchRequestByAuthor.isEmpty()) {
                page = bookService.getAllBooksBySearchQueryByAuthorPaginated(
                        searchRequestByAuthor, pageNo, pageSize, sortField, sortDir);
            } else if (!searchRequestByTitle.isEmpty()) {
                page = bookService.getAllBooksBySearchQueryByTitlePaginated(
                        searchRequestByTitle, pageNo, pageSize, sortField, sortDir);
            }
            List<Book> books = page.getContent();

            // Creating the model response.
            // Note for simplicity purpose we are not making the use of ResponseDto here.
            // In ideal cases the response will be encapsulated in a class.
            // pagination parameters
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());
            // sorting parameters
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
            // books
            model.addAttribute("books", books);
            // dto for searching
            model.addAttribute("searchQueryByAuthor", new SearchQueryDTO(searchRequestByAuthor));
            model.addAttribute("searchRequestByAuthor", searchRequestByAuthor);
            model.addAttribute("searchQueryByTitle", new SearchQueryDTO(searchRequestByTitle));
            model.addAttribute("searchRequestByTitle", searchRequestByTitle);
            return "bookCatalog/bookCatalog";
        } catch (Exception ex) {
            log.info("Somethings went wrong. Message: {}", ex.getMessage());
            model.addAttribute("message", "Somethings went wrong. Message: " + ex.getMessage());
            return "error";
        }
    }

    @PostMapping(value = {"findBooksByAuthor"})
    public String findBooksByAuthor(@ModelAttribute("searchQueryByAuthor") SearchQueryDTO searchQueryByAuthor) {
        log.info("Redirecting to bookCatalog/page with search query by author : " + searchQueryByAuthor.getContent());
        return "redirect:page/1?sort-field=id&sort-dir=asc&search-request-by-author="
                + searchQueryByAuthor.getContent() + "&search-request-by-title=";
    }

    @PostMapping(value = {"findBooksByTitle"})
    public String findBooksByTitle(@ModelAttribute("searchQueryByTitle") SearchQueryDTO searchQueryByTitle) {
        log.info("Redirecting to bookCatalog/page with search query by title : " + searchQueryByTitle.getContent());
        return "redirect:page/1?sort-field=id&sort-dir=asc&search-request-by-author=&search-request-by-title="
                + searchQueryByTitle.getContent();
    }

}
