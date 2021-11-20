package ua.online.onlineua_final_project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.online.onlineua_final_project.entity.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
    List<Book> findAll();

    Optional<Book> findByIsbn(String isbn);

    @Override
    void deleteById(Long aLong);

    @Override
    Optional<Book> findById(Long aLong);

    Page<Book> findAllByAuthorContains(String searchQueryByAuthor, Pageable pageable);

    Page<Book> findAllByTitleContains(String searchQueryByTitle, Pageable pageable);
}
