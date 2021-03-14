package sk.uniza.fri.askfri.service.implementation;

import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.IMessageRepository;
import sk.uniza.fri.askfri.model.Message;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.service.IMessageService;

import java.util.List;

@Service
public class MessageServiceImplement implements IMessageService {

    private final IMessageRepository messageRepository;

    public MessageServiceImplement(IMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public List<Message> findAllRoomMessages(Room room) {
        return this.messageRepository.findAllByIdRoom(room);
    }

    @Override
    public Message saveMessage(Message message) {
        return this.messageRepository.save(message);
    }

    @Override
    public void deleteMessage(Long idMessage) {
        this.messageRepository.deleteById(idMessage);
    }

    @Override
    public Message findByIdMessage(Long idMessage) {
        return this.messageRepository.findByIdMessage(idMessage);
    }
}
