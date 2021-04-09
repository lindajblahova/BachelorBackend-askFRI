package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.askfri.model.Message;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.dto.MessageDto;

import java.util.List;
import java.util.Set;

@Repository
public interface IMessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByIdRoomOrderByIdMessageDesc(Room room);
    Message findByIdMessage(Long idMessage);

//    @Query(value = "select message.idMessage,message.content,message.idRoom, count(LikedMessage.idMessage.idMessage) from message left join \n" +
//            "LikedMessage on message.idMessage=LikedMessage.idMessage.idMessage where message.idRoom.idRoom=?1 group by message.idMessage")
//    List<Message> selectMessagesWithLikes(Long idRoom);
}
