package ua.online.onlineua_final_project.bootstrap;

import com.github.javafaker.Faker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ua.online.onlineua_final_project.entity.Book;
import ua.online.onlineua_final_project.service.BookService;

import java.time.LocalDate;


@Slf4j
@RequiredArgsConstructor
@Component
public class DefaultBooksLoader implements CommandLineRunner {

    private final BookService service;
    private final Faker faker;

    @Override
    public void run(String... args) throws Exception {
        loadBooks();
    }

    private void loadBooks() {
        int count = 0;
        if (service.getTotalBooks() == 0) {
            for (int x = 0; x < 100; x++) {
                count = count + 1;
                service.save(createNewBook());
            }
            log.info("Total {} books are saved in the database.", count);
        } else {
            log.info("Default books are already present in the database.");
        }
    }

    private Book createNewBook() {
        String title = faker.book().title();
        String author = faker.book().author();
        String publisher = faker.book().publisher();
        LocalDate publishingDate = LocalDate.now();

        int random_4number_first = (int) Math.floor(Math.random() * (9999 - 1000 + 1) + 1000);
        int random_4number_second = (int) Math.floor(Math.random() * (9999 - 1000 + 1) + 1000);
        int random_1number_last = (int) Math.floor(Math.random() * (9 - 1 + 1) + 1);
        String isbn = "0-" + random_4number_first + "-" + random_4number_second + "-" + random_1number_last;

        int quantity = (int) Math.floor(Math.random() * (9 - 1 + 1) + 1);

        return Book.builder()
                .title(title)
                .author(author)
                .publisher(publisher)
                .publishingDate(publishingDate)
                .isbn(isbn)
                .quantity(quantity)
                .build();
    }
}
