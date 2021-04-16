package sk.uniza.fri.askfri.model;

import javax.persistence.*;

@Entity(name = "LikedMessage")
@Table(name = "liked_message")
@IdClass(LikedMessageId.class)
public class LikedMessage {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_profile_id_user", referencedColumnName = "idUser", nullable=false, updatable = false)
    private User idUser;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="message_id_message",referencedColumnName = "idMessage", nullable=false, updatable = false)
    private Message idMessage;

    public LikedMessage() {}

    public LikedMessage(User idUser, Message idMessage) {
        this.idUser = idUser;
        this.idMessage = idMessage;
    }

    public User getIdUser() {
        return idUser;
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    public Message getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(Message idMessage) {
        this.idMessage = idMessage;
    }

}
