package sk.uniza.fri.askfri.model.dto;

import sk.uniza.fri.askfri.model.Message;
import sk.uniza.fri.askfri.model.User;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class LikedMessageDto {

    private Long idLikedMessage;
    private Long idUser;
    private Long idMessage;

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(Long idMessage) {
        this.idMessage = idMessage;
    }

    public Long getIdLikedMessage() {
        return idLikedMessage;
    }

    public void setIdLikedMessage(Long idLikedMessage) {
        this.idLikedMessage = idLikedMessage;
    }
}
