package ua.online.onlineua_final_project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.online.onlineua_final_project.dto.NoteDTO;
import ua.online.onlineua_final_project.service.RegFormService;

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
    public void registrationFormController(NoteDTO note){
        log.info("{}", note);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}