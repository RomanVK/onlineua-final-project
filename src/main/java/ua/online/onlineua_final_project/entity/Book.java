package ua.online.onlineua_final_project.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode

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
    @ToString.Exclude
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "users_books",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @EqualsAndHashCode.Exclude
    private Set<User> usersWhoOrderedTheBook;

    // took from https://vladmihalcea.com/the-best-way-to-map-a-many-to-many-association-with-extra-columns-when-using-jpa-and-hibernate/
    @OneToMany(
            mappedBy = "book",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    private Set<UserBookSubscription> usersWhoHaveTheBookBySubscription = new HashSet<>();

    public void addUser(User user) {
        UserBookSubscription userBookSubscription = new UserBookSubscription(user, this);
        usersWhoHaveTheBookBySubscription.add(userBookSubscription);
        user.getBooksThatAreInTheUserSubscription().add(userBookSubscription);
    }

    public void removeUser(User user) {
        for (Iterator<UserBookSubscription> iterator = usersWhoHaveTheBookBySubscription.iterator();
             iterator.hasNext(); ) {
            UserBookSubscription userBookSubscription = iterator.next();

            if (userBookSubscription.getBook().equals(this) &&
                    userBookSubscription.getUser().equals(user)) {
                iterator.remove();
                userBookSubscription.getUser().getBooksThatAreInTheUserSubscription().remove(userBookSubscription);
                userBookSubscription.setBook(null);
                userBookSubscription.setUser(null);
            }
        }
    }

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "users_books_reading_room",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @EqualsAndHashCode.Exclude
    private Set<User> usersWhoHaveTheBookInReadingRoom;

}

