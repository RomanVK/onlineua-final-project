package ua.online.onlineua_final_project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.online.onlineua_final_project.dto.NoteDTO;
import ua.online.onlineua_final_project.entity.User;
import ua.online.onlineua_final_project.repository.UserRepository;

@Slf4j
@Service
public class RegFormService {
    private UserRepository userRepository;

    @Autowired
    public RegFormService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveNewUser (User user){
        try {
            userRepository.save(user);
        } catch (Exception ex){
            log.info("{This email address has occupied}");
            throw new RuntimeException("This email address has occupied");
        }

    }

    public String inputNote(NoteDTO name) {
        return "";
    }
}
