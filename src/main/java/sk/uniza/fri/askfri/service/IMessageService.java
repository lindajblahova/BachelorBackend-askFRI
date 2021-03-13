package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.Message;
import sk.uniza.fri.askfri.model.Room;

import java.util.List;

public interface IMessageService {

    List<Message> findAllRoomMessages(Room room);
    Message saveMessage(Message message);
    void deleteMessage(Long idMessage);
}
