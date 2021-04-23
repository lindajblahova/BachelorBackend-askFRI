package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.askfri.model.Message;

/** Repozitar pre CRUD operacie s tabulkou message
 * extends JpaRepository, pre typ Message a ID typu Long
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@Repository
public interface IMessageRepository extends JpaRepository<Message, Long> {

    /** Metoda pre ziskanie spravy podla ID spravy
     * @param idMessage (Long) ID spravy
     * @return Message najdena sprava/null
     */
    Message findByIdMessage(Long idMessage);

    /** Metoda pre zistenie ci je pouzivatel autorom miestnosti do ktorej sprava patri
     * @param idUser (Long) ID pouzivatela
     * @param idMessage (Long) ID spravy
     * @return Integer pocet riadkov najdenych podla podmienky
     */
    @Query("SELECT count(r.idOwner.idUser) FROM message m JOIN room r ON r.idRoom=m.idRoom.idRoom " +
            "WHERE r.idOwner.idUser=?1 AND r.idRoom IN " +
            "(SELECT m.idRoom.idRoom FROM message m WHERE m.idMessage=?2)")
    Integer isUserRoomOwner(Long idUser, Long idMessage);

}
