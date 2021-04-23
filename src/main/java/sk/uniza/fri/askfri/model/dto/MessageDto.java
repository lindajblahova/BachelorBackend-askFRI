package sk.uniza.fri.askfri.model.dto;

/** Trieda DTO udajov pre spravu v miestnosti
 * obsahuje ID spravy, ID miestnosti a obsah spravy
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
public class MessageDto {

    private Long idMessage;
    private Long idRoom;
    private String content;

    public MessageDto() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(Long idRoom) {
        this.idRoom = idRoom;
    }

    public Long getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(Long idMessage) {
        this.idMessage = idMessage;
    }

}
