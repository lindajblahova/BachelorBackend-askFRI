package sk.uniza.fri.askfri.model;

import javax.persistence.*;

/** Trieda mapovana na tabulku liked_message databazy, sluzi pre
 *  zaznamenanie reakcie pouzivatela na spravu
 *  obsahuje rodicovskeho pouzivatela a rodicovsku spravu
 *  ID je trieda LikedMessageId - KPK
 *  LikedMessage je vo vztahu ManyToOne k rodicovskej sprave, referencuje ID spravy
 *  LikedMessage je vo vztahu ManyToOne k rodicovskemu pouzivatelovi, referencuje
 *  ID pouzivatela
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@Entity(name = "LikedMessage")
@Table(name = "liked_message")
@IdClass(LikedMessageId.class)
public class LikedMessage {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_profile_id_user", referencedColumnName = "idUser", nullable=false)
    private User idUser;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="message_id_message",referencedColumnName = "idMessage", nullable=false)
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

}
