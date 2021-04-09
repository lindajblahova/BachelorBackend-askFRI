package sk.uniza.fri.askfri.model.dto;

import sk.uniza.fri.askfri.model.LikedMessage;
import sk.uniza.fri.askfri.model.Room;

import java.sql.Timestamp;
import java.util.Set;

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
