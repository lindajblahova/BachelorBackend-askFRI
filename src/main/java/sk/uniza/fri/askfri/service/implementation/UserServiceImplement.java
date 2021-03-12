package sk.uniza.fri.askfri.service.implementation;


import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.IUserRepository;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.service.IUserService;

import java.util.List;

@Service
public class UserServiceImplement implements IUserService {

    private IUserRepository userRepository;

    public UserServiceImplement(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User saveUser(User user) {
        this.userRepository.save(user);
        return user;
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public User getUser(String email) {
        User existingUser = this.userRepository.findByEmail(email);
        if (existingUser != null) {
            return existingUser;
        } else {
            //TODO: return exception
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAllByOrderBySurnameAsc();
    }

    @Override
    public void deleteUser(long id) {
            this.userRepository.deleteById(id);
    }
}
