package sk.uniza.fri.askfri.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name = "message")
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idMessage;

    @ManyToOne
    @JoinColumn(name="id_room", nullable=false)
    private Room idRoom;

    @Column(
            name = "content",
            updatable = false,
            columnDefinition = "TEXT"
    )
    private String content;

    /*@Column(
            name = "date",
            updatable = false,
            columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    private Timestamp date;*/

    public Message(Room idRoom, String content) {
        this.idRoom = idRoom;
        this.content = content;
    }

    public Message() {}

    public Long getIdRoom() {
        return idRoom.getIdRoom();
    }

    public void setIdRoom(Room idRoom) {
        this.idRoom = idRoom;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /*public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }*/

    public Long getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(Long idMessage) {
        this.idMessage = idMessage;
    }
}
