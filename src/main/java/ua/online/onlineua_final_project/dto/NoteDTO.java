package ua.online.onlineua_final_project.dto;

import lombok.*;
import ua.online.onlineua_final_project.validation.PasswordMatches;
import ua.online.onlineua_final_project.validation.ValidEmail;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@PasswordMatches
public class NoteDTO {
    @NonNull
    @NotEmpty
    private String firstName;

    @NonNull
    @NotEmpty
    private String lastName;

    @ValidEmail
    @NonNull
    @NotEmpty
    private String email;

    @NonNull
    @NotEmpty
    private String password;
    private String matchingPassword;

}