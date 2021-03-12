package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.uniza.fri.askfri.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
