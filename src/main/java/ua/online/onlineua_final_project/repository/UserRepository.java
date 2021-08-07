package ua.online.onlineua_final_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.online.onlineua_final_project.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository {
    Optional<User> findUserByEmail (String email);
}
