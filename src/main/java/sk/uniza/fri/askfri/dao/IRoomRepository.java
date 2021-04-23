package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.askfri.model.Room;

import java.util.Set;

/** Repozitar pre CRUD operacie s tabulkou room
 * extends JpaRepository, pre typ Room a ID typu Long
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@Repository
public interface IRoomRepository extends JpaRepository<Room, Long> {

    /** Metoda pre zistenie, ci existuje miestnost s danym kodom a aktivitou
     * @param roomPasscode (String) pristupovy kod miestnosti
     * @param isActive (boolean) aktivita miestnosti
     * @return boolean najdena miestnost=true, nenajdena miestnost=false
     */
    boolean existsByRoomPasscodeAndActive(String roomPasscode, boolean isActive);

    /** Metoda pre ziskanie miestnosti podla pristupoveho kodu a aktivity
     * @param roomPasscode (String) pristupovy kod miestnosti
     * @param isActive (boolean) aktivita miestnosti
     * @return Room najdena miestnost/null
     */
    Room findByRoomPasscodeAndActive(String roomPasscode, boolean isActive);


    /** Metoda pre ziskanie miestnosti podla ID miestnosti
     * @param idRoom (Long) ID miestnosti
     * @return Room najdena miestnost/null
     */
    Room findRoomByIdRoom(Long idRoom);

    /** Metoda pre ziskanie vsetkych miestnosti
     * @return Set<Room> Set najdenych miestnosti
     */
    Set<Room> findAllByOrderByIdRoomDesc();

    /** Metoda pre zistenie ci je pouzivatel autorom miestnosti
     * @param idUser (Long) ID pouzivatela
     * @param idRoom (Long) ID miestnosti
     * @return Integer pocet riadkov najdenych podla podmienky
     */
    @Query("SELECT count(r.idOwner.idUser) FROM room r WHERE r.idOwner.idUser=?1 AND r.idRoom=?2")
    Integer isUserRoomOwner(Long idUser, Long idRoom);
}

