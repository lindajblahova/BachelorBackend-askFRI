package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.Message;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.dto.MessageDto;

import java.util.List;
import java.util.Set;

public interface IMessageService {

    List<Message> findAllRoomMessages(Room room);
    Message saveMessage(Message message);
    void deleteMessage(Long idMessage);
    Message findByIdMessage(Long idMessage);

//    List<Message> selectMessagesWithLikes(Long idRoom);
}
