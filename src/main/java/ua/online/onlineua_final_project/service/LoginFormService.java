package ua.online.onlineua_final_project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.online.onlineua_final_project.dto.UserDTO;
import ua.online.onlineua_final_project.entity.User;
import ua.online.onlineua_final_project.repository.UserRepository;

import java.util.Optional;

@Slf4j
@Service
public class LoginFormService {
    private UserRepository userRepository;

    @Autowired
    public LoginFormService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findUserByEmail (UserDTO userDTO){
        return userRepository.findUserByEmail(userDTO.getEmail());
    }

    public void saveNewUser (User user){
        try {
            userRepository.save(user);
        } catch (Exception ex){
            log.info("{Почтовый адрес уже существует}");
        }

    }

    public String inputUser(UserDTO name) {
        return "";
    }
}
