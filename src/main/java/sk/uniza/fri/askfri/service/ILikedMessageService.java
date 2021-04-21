package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.LikedMessage;
import sk.uniza.fri.askfri.model.dto.LikedMessageDto;
import sk.uniza.fri.askfri.model.dto.ResponseDto;

public interface ILikedMessageService {

    ResponseDto saveLikedMessage(LikedMessageDto likedMessageDto);
    ResponseDto deleteLikedMessage(Long idMessage,Long idUser);

}
