package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.askfri.model.User;

import java.util.Set;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    boolean existsByEmail(String email);
    User findByIdUser(Long idUser);
    Set<User> findAllByOrderBySurnameAsc();

    /*
    @Modifying
    @Query("update user_profile u set u.password = ?1 where u.idUser = ?2")
    void setUserPasswordById(String password, Long userId);*/
}
