package ua.online.onlineua_final_project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.online.onlineua_final_project.entity.RoleType;
import ua.online.onlineua_final_project.entity.User;
import ua.online.onlineua_final_project.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
public class LibrarianService {
    private UserRepository userRepository;

    @Autowired
    public LibrarianService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAllByRole(RoleType.USER);
        return users;
    }
}
