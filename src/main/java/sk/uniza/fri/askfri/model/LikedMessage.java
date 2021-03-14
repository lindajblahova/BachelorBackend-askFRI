package sk.uniza.fri.askfri.model;

import javax.persistence.*;

@Entity(name = "LikedMessage")
@Table(name = "liked_message")
public class LikedMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idLikedMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_user", nullable=false)
    private User idUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_message", nullable=false)
    private Message idMessage;

    public LikedMessage() {}

    public LikedMessage(User idUser, Message idMessage) {
        this.idUser = idUser;
        this.idMessage = idMessage;
    }

    public Long getIdUser() {
        return idUser.getIdUser();
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    public Long getIdMessage() {
        return idMessage.getIdMessage();
    }

    public void setIdMessage(Message idMessage) {
        this.idMessage = idMessage;
    }

    public Long getIdLikedMessage() {
        return idLikedMessage;
    }

    public void setIdLikedMessage(Long idLikedMessage) {
        this.idLikedMessage = idLikedMessage;
    }
}
