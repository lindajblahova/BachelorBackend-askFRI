package sk.uniza.fri.askfri.service.implementation;

import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.ILikedMessageRepository;
import sk.uniza.fri.askfri.model.LikedMessage;
import sk.uniza.fri.askfri.service.ILikedMessageService;

import java.util.List;

@Service
public class LikedMessageServiceImplement implements ILikedMessageService {

    private final ILikedMessageRepository likedMessageRepository;

    public LikedMessageServiceImplement(ILikedMessageRepository likedMessageRepository) {
        this.likedMessageRepository = likedMessageRepository;
    }

    @Override
    public LikedMessage saveMessageLike(LikedMessage likedMessage) {
        return this.likedMessageRepository.save(likedMessage);
    }

    @Override
    public Integer messageLikesCount(Long idMessage) {
        return this.likedMessageRepository.countAllByIdMessage_IdMessage(idMessage);
    }

    @Override
    public List<Integer> userLikedMessages(Long idUser) {
        return this.likedMessageRepository.findAllByIdUser_IdUser(idUser);
    }

    @Override
    public void deleteMessage(Long idLikedMessage) {
        this.likedMessageRepository.deleteById(idLikedMessage);
    }
}
