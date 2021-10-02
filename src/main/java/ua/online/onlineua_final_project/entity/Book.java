package ua.online.onlineua_final_project.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

@Entity
@Table(name = "book",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"isbn"})})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "author", nullable = false)
    private String author;
    @Column(name = "publisher", nullable = false)
    private String publisher;
    @Column(name = "publishing_date", nullable = false)
    private LocalDate publishingDate;
    @Column(name = "isbn", nullable = false)
    private String isbn;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}

