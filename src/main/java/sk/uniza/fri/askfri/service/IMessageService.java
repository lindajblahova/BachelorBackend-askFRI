package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.LikedMessage;
import sk.uniza.fri.askfri.model.Message;
import sk.uniza.fri.askfri.model.dto.MessageDto;
import sk.uniza.fri.askfri.model.dto.MessageWithLikes;
import sk.uniza.fri.askfri.model.dto.ResponseDto;

import java.util.Set;

public interface IMessageService {

    ResponseDto createMessage(MessageDto messageDto);
    Message saveMessage(Message message);
    Message saveMessageFlush(Message message);
    Set<MessageWithLikes> getAllRoomMessages(Long idRoom);
    ResponseDto deleteMessage(Long idMessage);

    Message findByIdMessage(Long idMessage);

}
