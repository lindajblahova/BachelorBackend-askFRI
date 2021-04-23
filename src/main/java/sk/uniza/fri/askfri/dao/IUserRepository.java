package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.askfri.model.User;
import java.util.Set;

/** Repozitar pre CRUD operacie s tabulkou user_profile
 * extends JpaRepository, pre typ User a ID typu Long
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    /** Metoda pre ziskanie pouzivatela podla emailu
     * @param email (String)
     * @return User najdeny pouzivatel/null
     */
    User findByEmail(String email);

    /** Metoda pre zistenie, ci pouzivatel s danym emailom existuje
     * @param email (String)
     * @return boolean najdeny pouzivatel=true, nenajdeny=false
     */
    boolean existsByEmail(String email);

    /** Metoda pre ziskanie pouzivatela podla jeho ID
     * @param idUser (Long)
     * @return User najdeny pouzivatel/null
     */
    User findByIdUser(Long idUser);

    /** Metoda pre ziskanie vsetkych pouzivatelov zoradenych abecedne podla priezviska
     * @return Set<User> Set vsetkych pouzivatelov
     */
    Set<User> findAllByOrderBySurnameAsc();
}
