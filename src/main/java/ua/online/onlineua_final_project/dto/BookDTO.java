package ua.online.onlineua_final_project.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BookDTO {
    @NonNull
    @NotEmpty
    private String title;
    @NonNull
    @NotEmpty
    private String author;
    @NonNull
    @NotEmpty
    private String publisher;
    @NonNull
    @PastOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishingDate;
    @NonNull
    @NotEmpty
    private String isbn;
    @NonNull
    @Min(value = 1, message = "Quantity should not be less than 1")
    private Integer quantity;

}
