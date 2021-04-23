package sk.uniza.fri.askfri.model.dto;

/** Trieda DTO udajov pre zaznam reakcie na spravu pouzivatelom
 * obsahuje ID pouzivatela, ID spravy
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
public class LikedMessageDto {

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
}
