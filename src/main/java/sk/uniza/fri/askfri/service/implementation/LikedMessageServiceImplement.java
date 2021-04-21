package sk.uniza.fri.askfri.service.implementation;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.ILikedMessageRepository;
import sk.uniza.fri.askfri.model.LikedMessage;
import sk.uniza.fri.askfri.model.LikedMessageId;
import sk.uniza.fri.askfri.model.Message;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.LikedMessageDto;
import sk.uniza.fri.askfri.model.dto.ResponseDto;
import sk.uniza.fri.askfri.service.ILikedMessageService;
import sk.uniza.fri.askfri.service.IMessageService;
import sk.uniza.fri.askfri.service.IUserService;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
public class LikedMessageServiceImplement implements ILikedMessageService {

    private final IUserService userService;
    private final IMessageService messageService;
    private final ILikedMessageRepository likedMessageRepository;

    public LikedMessageServiceImplement(IUserService userService, IMessageService messageService, ILikedMessageRepository likedMessageRepository) {
        this.userService = userService;
        this.messageService = messageService;
        this.likedMessageRepository = likedMessageRepository;
    }

    @Override
    public ResponseDto saveLikedMessage(LikedMessageDto likedMessageDto) {
        Message parentMessage = this.messageService.findByIdMessage(likedMessageDto.getIdMessage());
        User parentUser = this.userService.findUserByIdUser(likedMessageDto.getIdUser());
        if (parentMessage != null && parentUser != null) {
            LikedMessage likedMessage = new LikedMessage(parentUser,parentMessage);
            likedMessage = this.likedMessageRepository.save(likedMessage);
            parentUser.addLikedMessage(likedMessage);
            parentMessage.addLikedMessage(likedMessage);
            this.userService.saveUser(parentUser);
            this.messageService.saveMessage(parentMessage);
            return new ResponseDto(likedMessage.hashCode(),"Like bol zaznamenaný");

        }
        throw new NullPointerException("Nebolo možné odoslať reakciu");
    }

    @Override
    public ResponseDto deleteLikedMessage(Long idMessage,Long idUser) {
        try
        { ;
            User user = this.userService.findUserByIdUser(idUser);
            Message message = this.messageService.findByIdMessage(idMessage);
            LikedMessageId likedMessageId = new LikedMessageId(idUser,idMessage);
            LikedMessage likedMessage = this.likedMessageRepository.findLikedMessageByIdUser_IdUserAndIdMessage_IdMessage(idUser, idMessage);

            user.removeLikedMessage(likedMessage);
            message.removeLikedMessage(likedMessage);
            this.likedMessageRepository.deleteById(likedMessageId);
            this.userService.saveUser(user);
            this.messageService.saveMessage(message);
            return new ResponseDto(likedMessageId.hashCode(), "Reakcia bola zrušená");
        } catch (EmptyResultDataAccessException e)
        {
            return null;
        }
    }
}
