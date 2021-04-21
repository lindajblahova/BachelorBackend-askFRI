package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.askfri.model.Message;
import sk.uniza.fri.askfri.model.Room;

import java.util.Set;

@Repository
public interface IMessageRepository extends JpaRepository<Message, Long> {

  /*  @EntityGraph(value = "roomMessages", type = EntityGraph.EntityGraphType.LOAD)
    List<Message> findMessagesByIdRoom(Room room);*/

    Message findByIdMessage(Long idMessage);

    @Modifying
    @Query("DELETE FROM message m WHERE m.idRoom.idRoom=?1")
    void deleteMessagesByIdRoom_IdRoom(Room room);

    @Modifying
    @Query("DELETE FROM message m WHERE m.idRoom.idRoom=?1")
    void deleteAllByIdRoom_IdRoom(Long idRoom);

    /*@Modifying
    @Query("delete from message m where m.idRoom.idRoom=?1")
    void deleteMessagesByIdRoom_IdRoom(Long idRoom);*/

//    @Query(value = "select message.idMessage,message.content,message.idRoom, count(LikedMessage.idMessage.idMessage) from message left join \n" +
//            "LikedMessage on message.idMessage=LikedMessage.idMessage.idMessage where message.idRoom.idRoom=?1 group by message.idMessage")
//    List<Message> selectMessagesWithLikes(Long idRoom);
}
