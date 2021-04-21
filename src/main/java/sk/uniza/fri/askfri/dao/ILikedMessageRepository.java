package sk.uniza.fri.askfri.dao;

import org.hibernate.persister.entity.Loadable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import sk.uniza.fri.askfri.model.LikedMessage;
import sk.uniza.fri.askfri.model.LikedMessageId;
import sk.uniza.fri.askfri.model.Message;
import sk.uniza.fri.askfri.model.User;

import java.util.Set;

public interface ILikedMessageRepository extends JpaRepository<LikedMessage, LikedMessageId> {

    LikedMessage findLikedMessageByIdUser_IdUserAndIdMessage_IdMessage(Long idUser, Long idMessage);

    @Modifying
    @Query("DELETE FROM LikedMessage m WHERE m.idMessage=?1")
    void deleteLikedMessagesByIdMessage_IdMessage(Message message);

    @Modifying
    @Query("DELETE FROM LikedMessage m WHERE m.idUser=?1")
    void deleteLikedMessagesByIdUser_IdUser(User idUser);


    @Modifying
    @Query("DELETE FROM LikedMessage m WHERE m.idMessage.idMessage=?1")
    void deleteAllByIdMessage_IdMessage(Long idMessage);

    @Modifying
    @Query("DELETE FROM LikedMessage m WHERE m.idUser.idUser=?1")
    void deleteAllByIdUser_IdUser(Long idUser);

}
