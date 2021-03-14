package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.LikedMessage;

import java.util.List;

public interface ILikedMessageService {
    LikedMessage saveMessageLike(LikedMessage likedMessage);
    Integer messageLikesCount(Long idMessagw);
    List<Integer> userLikedMessages(Long idUser);
    void deleteMessage(Long idLikedMessage);
}
