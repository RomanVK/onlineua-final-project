package ua.online.onlineua_final_project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.online.onlineua_final_project.dto.NoteDTO;
import ua.online.onlineua_final_project.service.RegFormService;
import ua.online.onlineua_final_project.web.error.UserAlreadyExistException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/")
public class RegFormController {

    private final RegFormService regFormService;

    @Autowired
    public RegFormController(RegFormService regFormService) {
        this.regFormService = regFormService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "registration_form")
    public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid NoteDTO note,
                                            HttpServletRequest request, Errors errors) {
        log.info("Registering user account with information: {}", note);
        try {
            regFormService.registerNewUserAccount(note);
        } catch (UserAlreadyExistException uaeEx) {
            ModelAndView mav = new ModelAndView("registration_form", "user", note);
            mav.addObject("message", "An account for that username/email already exists.");
            return mav;
        }
        return new ModelAndView("login", "user", note);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}