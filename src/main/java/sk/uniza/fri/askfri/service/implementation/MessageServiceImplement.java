package sk.uniza.fri.askfri.service.implementation;

import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.ILikedMessageRepository;
import sk.uniza.fri.askfri.dao.IMessageRepository;
import sk.uniza.fri.askfri.model.LikedMessage;
import sk.uniza.fri.askfri.model.Message;
import sk.uniza.fri.askfri.service.IMessageService;

import javax.transaction.Transactional;

@Service
public class MessageServiceImplement implements IMessageService {

    private final IMessageRepository messageRepository;
    private final ILikedMessageRepository likedMessageRepository;

    public MessageServiceImplement(IMessageRepository messageRepository, ILikedMessageRepository likedMessageRepository) {
        this.messageRepository = messageRepository;
        this.likedMessageRepository = likedMessageRepository;
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

    @Override
    public LikedMessage findLikedMessage(Long idUser, Long idMessage) {
        return this.likedMessageRepository.findLikedMessageByIdUser_IdUserAndIdMessage_IdMessage(idUser, idMessage);
    }

  /*  @Transactional
    @Override
    public void deleteMessagesByIdRoom(Long idRoom) {
        this.messageRepository.deleteMessagesByIdRoom_IdRoom(idRoom);
    }*/

//    @Override
//    public List<Message> selectMessagesWithLikes(Long idRoom) {
//        return this.messageRepository.selectMessagesWithLikes(idRoom);
//    }
}
