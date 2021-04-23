package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.*;
import sk.uniza.fri.askfri.model.dto.user.UserDto;
import sk.uniza.fri.askfri.model.dto.user.UserPasswordDto;
import sk.uniza.fri.askfri.model.dto.user.UserProfileDto;

import javax.persistence.EntityExistsException;
import java.util.Set;

/** Interface pre sluzbu pracujucu s User
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
public interface IUserService {

    /** Metoda pre vytvorenie pouzivatela
     * @param userDto DTO pouzivatel
     * @return ResponseDto odpoved o vytvoreni pouzivatela
     * @throws IllegalArgumentException ak boli zle zadane udaje
     * @throws EntityExistsException ak je zadany email uz registrovany
     */
    ResponseDto createUser(UserDto userDto);

    /** Metoda pre ulozenie pouzivatela
     * @param user pouzivatel
     * @return User ulozeny pouzivatel
     */
    User saveUser(User user);

    /** Metoda pre najdenie pouzivatela s danym emailom
     * @param email hladany email
     * @return User najdeny pouzivatel
     */
    User getUserByEmail(String email);

    /** Metoda pre najdenie pouzivatela podla jeho ID
     * @param idUser ID pouzivatela
     * @return User najdeny pouzivatel
     */
    User findUserByIdUser(Long idUser);

    /** Metoda pre najdenie profilovych udajov pouzivatela podla jeho ID
     * @param idUser ID pouzivatela
     * @return UserProfileDto najdene profilove udaje pouzivatela
     * @throws NullPointerException ak nebol najdeny pouzivatel
     */
    UserProfileDto getUserProfileByIdUser(Long idUser);

    /** Metoda pre upravenie hesla pouzivatela
     * @param userPasswordDto DTO pre zmenu hesla (UserPasswordDto)
     * @return ResponseDto odpoved o zmene hesla pouzivatela
     * @throws IllegalArgumentException ak zadane heslo nebolo spravne
     * @throws NullPointerException ak pouzivatel nebol najdeny
     */
    ResponseDto updateUserPassword(UserPasswordDto userPasswordDto);

    /** Metoda pre najdenie setu profilovych udajov vsetkych pouzivatelov
     * @return Set<UserProfileDto> set profilovych udajov vsetkych pouzivatelov
     */
    Set<UserProfileDto> getAllUsers();

    /** Metoda pre zmazanie pouzivatela
     * @param idUser ID pouzivatela
     * @return ResponseDto odpoved o zmazani pouzivatela
     */
    ResponseDto deleteUser(Long idUser);

}
