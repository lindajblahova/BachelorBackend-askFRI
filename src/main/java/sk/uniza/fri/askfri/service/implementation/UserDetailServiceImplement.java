package sk.uniza.fri.askfri.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.IUserRepository;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.UserDetailsDto;

@Service
public class UserDetailServiceImplement implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    public UserDetailsDto loadUserByUsername(String email) throws UsernameNotFoundException {
        User user  = userRepository.findByEmail(email);
        return new UserDetailsDto(user);
    }
}
