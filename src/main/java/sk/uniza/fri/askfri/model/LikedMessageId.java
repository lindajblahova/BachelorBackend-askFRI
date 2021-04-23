package sk.uniza.fri.askfri.model;

import java.io.Serializable;
import java.util.Objects;

/** Trieda reprezentujuca ID pre LikedMessage
 *  implementuje Serializable
 *  obsahuje ID rodicovskeho pouzivatela a ID rodicovskej spravy
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
public class LikedMessageId implements Serializable {
    private Long idUser;
    private Long idMessage;

    public LikedMessageId(Long idUser, Long idMessage) {
        this.idUser = idUser;
        this.idMessage = idMessage;
    }

    public LikedMessageId() {
    }

    public Long getIdUser() {
        return idUser;
    }

    public Long getIdMessage() {
        return idMessage;
    }
}
