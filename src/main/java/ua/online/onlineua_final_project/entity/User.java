package ua.online.onlineua_final_project.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

import static java.time.temporal.ChronoUnit.DAYS;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

@Entity
@Table(name = "user",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String email;
    @ToString.Exclude
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RoleType role;
    @ToString.Exclude
    @Column(nullable = false)
    private String password;
    @ToString.Exclude
    @Column(name = "non_locked", nullable = false)
    private boolean nonLocked;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "users_books",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    @EqualsAndHashCode.Exclude
    private Set<Book> booksThatAreInUserOrder;

    // took from https://vladmihalcea.com/the-best-way-to-map-a-many-to-many-association-with-extra-columns-when-using-jpa-and-hibernate/
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    private Set<UserBookSubscription> booksThatAreInTheUserSubscription;

    public void updatePenalty() {
        for (UserBookSubscription bookThatIsInTheUserSubscription : booksThatAreInTheUserSubscription) {
            LocalDate returnDate = bookThatIsInTheUserSubscription.getReturnDate();
            if (returnDate.isBefore(LocalDate.now())) {
                long numberOfDays = DAYS.between(returnDate, LocalDate.now());
                double amountPenalty = numberOfDays * 30.0;
                bookThatIsInTheUserSubscription.setAmountOfPenalty(amountPenalty);

                Book book = bookThatIsInTheUserSubscription.getBook();
                Set<UserBookSubscription> usersWhoHaveTheBookBySubscription = book.getUsersWhoHaveTheBookBySubscription();
                for (UserBookSubscription userWhoHaveTheBookBySubscription : usersWhoHaveTheBookBySubscription) {
                    if (userWhoHaveTheBookBySubscription.getBook().equals(book) &&
                            userWhoHaveTheBookBySubscription.getUser().equals(this)) {
                        userWhoHaveTheBookBySubscription.setAmountOfPenalty(amountPenalty);
                    }
                }
            }
        }
    }

    public void payPenalty(Book selectedBook) {
        for (UserBookSubscription bookThatAreInTheUserSubscription : booksThatAreInTheUserSubscription) {
            if (bookThatAreInTheUserSubscription.getBook().equals(selectedBook) &&
                    bookThatAreInTheUserSubscription.getUser().equals(this)) {
                bookThatAreInTheUserSubscription.setReturnDate(LocalDate.now());
                bookThatAreInTheUserSubscription.setAmountOfPenalty(0.0);

                Set<UserBookSubscription> usersWhoHaveTheBookBySubscription = selectedBook.getUsersWhoHaveTheBookBySubscription();
                for (UserBookSubscription userWhoHaveTheBookBySubscription : usersWhoHaveTheBookBySubscription) {
                    if (userWhoHaveTheBookBySubscription.getBook().equals(selectedBook) &&
                            userWhoHaveTheBookBySubscription.getUser().equals(this)) {
                        userWhoHaveTheBookBySubscription.setAmountOfPenalty(0.0);
                        userWhoHaveTheBookBySubscription.setReturnDate(LocalDate.now());
                        return;
                    }
                }
            }
        }
    }

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "users_books_reading_room",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    @EqualsAndHashCode.Exclude
    private Set<Book> booksThatAreInTheReadingRoom;
}