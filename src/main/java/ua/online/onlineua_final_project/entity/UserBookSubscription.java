package ua.online.onlineua_final_project.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode

@Entity
@Table(name = "users_books_subscriptions")
public class UserBookSubscription {

    @EmbeddedId
    private UserBookSubscriptionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookId")
    private Book book;

    @EqualsAndHashCode.Exclude
    @Column(name = "return_date")
    private LocalDate returnDate = LocalDate.now();

    @EqualsAndHashCode.Exclude
    @Column(name = "amount_penalty")
    private Double amountOfPenalty = 0.0;

    public UserBookSubscription(User user, Book book) {
        this.user = user;
        this.book = book;
        this.id = new UserBookSubscriptionId(user.getId(), book.getId());
    }


}
