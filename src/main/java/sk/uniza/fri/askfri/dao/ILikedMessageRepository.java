package sk.uniza.fri.askfri.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.askfri.model.LikedMessage;
import sk.uniza.fri.askfri.model.LikedMessageId;

/** Repozitar pre CRUD operacie s tabulkou liked_message
 * extends JpaRepository, pre typ LikedMessage a ID typu (KPK) LikedMessageId
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@Repository
public interface ILikedMessageRepository extends JpaRepository<LikedMessage, LikedMessageId> {

    /** Metoda pre ziskanie reakcie uzivatela na spravy podla ID uzivatela a ID spravy
     * @param idUser (Long) ID pouzivatela
     * @param idMessage (Long) ID spravy
     * @return LikedMessage najdena reakcia/null
     */
    LikedMessage findLikedMessageByIdUser_IdUserAndIdMessage_IdMessage(Long idUser, Long idMessage);
}
