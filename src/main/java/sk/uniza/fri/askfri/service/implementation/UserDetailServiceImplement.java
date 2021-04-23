package sk.uniza.fri.askfri.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.IUserRepository;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.login.UserDetailsDto;

/** Sluzba pre najdenie pouzivatela podla jeho emailu pri prihlaseni
 * implementuje UserDetailsService
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@Service
public class UserDetailServiceImplement implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    /** Pokusi sa podla emailu najst pouzivatela a vratit vsetky jeho udaje spolocne
     * s autoritami
     * @param email email pouzivatela
     * @return UserDetailsDto
     * @throws UsernameNotFoundException pokial nebol najdeny pouzivatel s danym emailom
     */
    public UserDetailsDto loadUserByUsername(String email) throws UsernameNotFoundException {
        User user  = this.userRepository.findByEmail(email);
        return new UserDetailsDto(user);
    }
}
