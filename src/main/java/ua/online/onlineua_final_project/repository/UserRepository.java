package ua.online.onlineua_final_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.online.onlineua_final_project.entity.RoleType;
import ua.online.onlineua_final_project.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail (String email);
    List<User> findAllByRole(RoleType roleType);

    @Override
    void deleteById(Long aLong);

    @Override
    <S extends User> S save(S s);
}
