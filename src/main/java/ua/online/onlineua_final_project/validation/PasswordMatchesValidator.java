package ua.online.onlineua_final_project.validation;

import ua.online.onlineua_final_project.dto.NoteDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        final NoteDTO user = (NoteDTO) obj;
        return user.getPassword().equals(user.getMatchingPassword());
    }

}