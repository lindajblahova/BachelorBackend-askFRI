package sk.uniza.fri.askfri.model;

import java.io.Serializable;
import java.util.Objects;

public class LikedMessageId implements Serializable {
    private Long idUser;
    private Long idMessage;

    public LikedMessageId(Long idUser, Long idMessage) {
        this.idUser = idUser;
        this.idMessage = idMessage;
    }

    public LikedMessageId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikedMessageId that = (LikedMessageId) o;
        return Objects.equals(idUser, that.idUser) &&
                Objects.equals(idMessage, that.idMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, idMessage);
    }

    public Long getIdUser() {
        return idUser;
    }

    public Long getIdMessage() {
        return idMessage;
    }
}
