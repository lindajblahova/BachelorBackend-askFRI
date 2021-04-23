package sk.uniza.fri.askfri.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.*;
import sk.uniza.fri.askfri.model.*;
import sk.uniza.fri.askfri.model.dto.*;
import sk.uniza.fri.askfri.model.dto.user.UserDto;
import sk.uniza.fri.askfri.model.dto.user.UserPasswordDto;
import sk.uniza.fri.askfri.model.dto.user.UserProfileDto;
import sk.uniza.fri.askfri.service.IUserService;

import javax.persistence.EntityExistsException;
import java.util.Set;
import java.util.stream.Collectors;

/** Sluzba pracujuca s User
 * implementuje IMessageService
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@Service
public class UserServiceImplement implements IUserService {

    private final IUserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImplement(IUserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /** Premapuje prijate udaje na User-a a pokial su v poriadku a dany email
     *  este nie je registrovany zakoduje heslo pouzivatela a ulozi ho.
     *  Vrati odpoved o vytvoreni
     * @param userDto DTO pouzivatel
     * @return ResponseDto odpoved o vytvoreni pouzivatela
     * @throws IllegalArgumentException ak boli zle zadane udaje
     * @throws EntityExistsException ak je zadany email uz registrovany
     */
    @Override
    public ResponseDto createUser(UserDto userDto) {
        User newUser = modelMapper.map(userDto, User.class);
        if (!newUser.getEmail().trim().equals("") && !newUser.getFirstname().trim().equals("") &&
                !newUser.getSurname().trim().equals("") && !newUser.getPassword().trim().equals("") ) {
            if (this.userRepository.existsByEmail(userDto.getEmail())) {
                throw new EntityExistsException("Email je už registrovaný");
            }
            if (newUser.getEmail().contains("@fri.uniza.sk") ||
                    newUser.getEmail().contains("@fstroj.uniza.sk") ||
                    newUser.getEmail().contains("@fpedas.uniza.sk") ||
                    newUser.getEmail().contains("@fhv.uniza.sk") ||
                    newUser.getEmail().contains("@fbi.uniza.sk") ||
                    newUser.getEmail().contains("@svf.uniza.sk") ||
                    newUser.getEmail().contains("@feit.uniza.sk")) {
                newUser.setRole("Vyucujuci");
            } else {
                newUser.setRole("Student");
            }
            newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
            newUser = this.userRepository.save(newUser);
            return new ResponseDto(newUser.getIdUser(), "Pokračujte prihlásením");
        }
        throw new IllegalArgumentException("Používateľa nebolo možné zaregistrovať");
    }

    /** Metoda pre ulozenie pouzivatela
     * @param user pouzivatel
     * @return User ulozeny pouzivatel
     */
    @Override
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    /** Pokusi sa najst pouzivatela pomocou emailu. Vrati najdeneho
     * pouzivatela ak nie je null
     * @param email hladany email
     * @return User najdeny pouzivatel
     * @throws NullPointerException ak pouzivatel s danym emailom neexistuje
     */
    @Override
    public User getUserByEmail(String email) {
        User existingUser = this.userRepository.findByEmail(email);
        if (existingUser != null) {
            return existingUser;
        }
        throw new NullPointerException("Používateľ nie je registrovaný");
    }

    /** Metoda pre najdenie pouzivatela podla jeho ID
     * @param idUser ID pouzivatela
     * @return User najdeny pouzivatel
     */
    @Override
    public User findUserByIdUser(Long idUser) {
        return this.userRepository.findByIdUser(idUser);
    }


    /** Pokusi sa najst pouzivatela podla ID, premapuje ho na UserProfileDto a vrati,
     * ak nie je null
     * @param idUser ID pouzivatela
     * @return UserProfileDto profilove udaje najdeneho pouzivatela
     * @throws NullPointerException ak nebol najdeny pouzivatel
     */
    @Override
    public UserProfileDto getUserProfileByIdUser(Long idUser) {
        User user = this.userRepository.findByIdUser(idUser);
        if (user != null)
        {
            return this.modelMapper.map(user, UserProfileDto.class);
        }
        throw new NullPointerException("Používateľ nebol nájdený!");
    }

    /** Pokusi sa najst pouzivatela a nasledne overi ci zadane aktualne heslo sedi s
     *  ulozenym heslom a tiez ci nove heslo nie je prazdne. Nasledne pouzivatelovi
     *  nastavi nove heslo ktore zakoduje a ulozi pouzivatela
     * @param userPasswordDto DTO pre zmenu hesla (UserPasswordDto)
     * @return ResponseDto odpoved o zmene hesla pouzivatela
     * @throws IllegalArgumentException ak zadane heslo nebolo spravne
     * @throws NullPointerException ak pouzivatel nebol najdeny
     */
    @Override
    public ResponseDto updateUserPassword(UserPasswordDto userPasswordDto) {
        User foundUser = this.userRepository.findByIdUser(userPasswordDto.getIdUser());
        if (foundUser != null) {
            if (this.passwordEncoder.matches(userPasswordDto.getOldPassword(),foundUser.getPassword())
                    && !userPasswordDto.getNewPassword().trim().equals(""))
            {
                foundUser.setPassword(this.passwordEncoder.encode(userPasswordDto.getNewPassword()));
                this.userRepository.save(foundUser);
                return new ResponseDto(userPasswordDto.getIdUser(), "Heslo bolo zmenené");
            }

            throw new IllegalArgumentException("Zadané heslo nebolo správne");
        }
        throw new NullPointerException("Používateľ neexistuje");
    }

    /** Najde vsetkych pouzivatelov, premapuje ich na profilove udaje a vrati
     * @return Set<UserProfileDto> set profilovych udajov vsetkych pouzivatelov
     */
    @Override
    public Set<UserProfileDto> getAllUsers() {
        return this.userRepository.findAllByOrderBySurnameAsc()
                .stream()
                .map( user -> modelMapper.map(user, UserProfileDto.class))
                .collect(Collectors.toSet());
    }

    /** Metoda pre zmazanie pouzivatela
     * @param idUser ID pouzivatela
     * @return ResponseDto odpoved o zmazani pouzivatela
     */
    @Override
    public ResponseDto deleteUser(Long idUser) {
        this.userRepository.deleteById(idUser);
        return new ResponseDto(idUser,"Používateľ bol vymazaný");
    }
}
