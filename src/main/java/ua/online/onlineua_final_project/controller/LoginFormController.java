package ua.online.onlineua_final_project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.online.onlineua_final_project.dto.UserDTO;
import ua.online.onlineua_final_project.entity.RoleType;
import ua.online.onlineua_final_project.entity.User;
import ua.online.onlineua_final_project.service.LoginFormService;

@Slf4j
@RestController
@RequestMapping(value = "/")
public class LoginFormController {

    private final LoginFormService loginFormService;

    @Autowired
    public LoginFormController(LoginFormService loginFormService) {
        this.loginFormService = loginFormService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "login")
    public void loginFormController(UserDTO user){
        log.info("{}", user);
        log.info("{}", loginFormService.findUserByEmail(user));
//        loginFormService.saveNewUser(User.builder()
//                .firstName("Roman")
//                .lastName("Kuzmin")
//                .email("roman.kuzmin3333@gmail.com")
//                .role(RoleType.USER)
//                .build());
    }
}