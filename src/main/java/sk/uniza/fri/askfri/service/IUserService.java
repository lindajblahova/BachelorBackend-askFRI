package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    User saveUser(User user);
    boolean existsByEmail(String email);
    User getUser(String email); //throws NotFoundException;
    List<User> getAllUsers();
    void deleteUser(long id);
}
