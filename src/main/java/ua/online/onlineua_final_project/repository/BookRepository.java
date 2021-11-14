package ua.online.onlineua_final_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.online.onlineua_final_project.entity.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAll();

    Optional<Book> findByIsbn(String isbn);

    @Override
    void deleteById(Long aLong);

    @Override
    Optional<Book> findById(Long aLong);

    List<Book> findAllByAuthorContains(String searchQueryByAuthor);
}
