package sk.uniza.fri.askfri.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.ILikedMessageRepository;
import sk.uniza.fri.askfri.dao.IMessageRepository;
import sk.uniza.fri.askfri.model.LikedMessage;
import sk.uniza.fri.askfri.model.Message;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.dto.LikeMesClass;
import sk.uniza.fri.askfri.model.dto.MessageDto;
import sk.uniza.fri.askfri.model.dto.MessageWithLikes;
import sk.uniza.fri.askfri.model.dto.ResponseDto;
import sk.uniza.fri.askfri.service.IMessageService;
import sk.uniza.fri.askfri.service.IRoomService;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
public class MessageServiceImplement implements IMessageService {

    private final IMessageRepository messageRepository;

    private final IRoomService roomService;
    private final ModelMapper modelMapper;

    public MessageServiceImplement(IMessageRepository messageRepository, IRoomService roomService, ModelMapper modelMapper) {
        this.messageRepository = messageRepository;
        this.roomService = roomService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseDto createMessage(MessageDto messageDto) {
        Room parentRoom = this.roomService.findByIdRoom(messageDto.getIdRoom());
        Message message = modelMapper.map(messageDto, Message.class);
        if (parentRoom != null && !messageDto.getContent().trim().equals("")) {
            parentRoom.addMessage(message);
            this.roomService.saveRoom(parentRoom);
            return new ResponseDto(message.getIdMessage(), "Správa bola vytvorená");
        }
        throw new NullPointerException("Správu sa nepodarilo vytovriť");
    }

    @Override
    public Message saveMessage(Message message) {
        return this.messageRepository.save(message);
    }

    @Override
    public Message saveMessageFlush(Message message) {
        return this.messageRepository.saveAndFlush(message);
    }

    @Override
    public Set<MessageWithLikes> getAllRoomMessages(Long idRoom) {
        Room parentRoom = this.roomService.findByIdRoom(idRoom);
        if (parentRoom != null)
        {
            Set<MessageWithLikes> setMessageWithLikes = new HashSet<MessageWithLikes>();
            Set<Message> setMessages = parentRoom.getMessagesSet();
            setMessages.forEach(message ->
            {
                Set<LikedMessage> likedMessageSet = message.getLikedMessageSet();
                Set<LikeMesClass> dtoSet = new HashSet<LikeMesClass>();
                if (likedMessageSet != null) {
                    likedMessageSet.forEach(data -> dtoSet.add(new LikeMesClass(data.getIdUser().getIdUser(),
                            data.getIdMessage().getIdMessage())));
                    setMessageWithLikes.add(new MessageWithLikes(
                            this.modelMapper.map(message, MessageDto.class),dtoSet));
                }
            });
            return setMessageWithLikes;
        }
        throw new NullPointerException("Správy miestnosti neboli nájdené");
    }

    @Override
    public ResponseDto deleteMessage(Long idMessage) {
        Message message = this.messageRepository.findByIdMessage(idMessage);
        Room parentRoom = this.roomService.findByIdRoom(message.getIdRoom());
        parentRoom.removeMessage(message);
        this.messageRepository.deleteById(idMessage); // TODO Added
        this.roomService.saveRoom(parentRoom);

        return new ResponseDto(idMessage, "Správa bola vymazaná");
    }


    @Override
    public Message findByIdMessage(Long idMessage) {
        return this.messageRepository.findByIdMessage(idMessage);
    }

}
