package sk.uniza.fri.askfri.dao;

import org.hibernate.persister.entity.Loadable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import sk.uniza.fri.askfri.model.LikedMessage;
import sk.uniza.fri.askfri.model.LikedMessageId;
import sk.uniza.fri.askfri.model.Message;

import java.util.Set;

public interface ILikedMessageRepository extends JpaRepository<LikedMessage, LikedMessageId> {

  /*  @EntityGraph(value = "messageLikes", type = EntityGraph.EntityGraphType.LOAD)
    Set<LikedMessage> findLikedMessagesByIdMessage_IdMessage(Long idMessage);
    Set<LikedMessage> findLikedMessagesByIdUser_IdUser(Long idUser);*/

    LikedMessage findLikedMessageByIdUser_IdUserAndIdMessage_IdMessage(Long idUser, Long idMessage);

}
