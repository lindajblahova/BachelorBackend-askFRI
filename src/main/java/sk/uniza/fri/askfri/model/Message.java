package sk.uniza.fri.askfri.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "message")
@Table(name = "message")
@NamedEntityGraph(name = "messageLikes",
        attributeNodes = @NamedAttributeNode("likedMessageSet"))
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_generator")
    @SequenceGenerator(name = "message_generator", sequenceName = "m_id_seq", allocationSize = 10)
    private Long idMessage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="room_id_room", referencedColumnName = "idRoom", nullable=false, updatable = false)
    private Room idRoom;

    @Column(
            name = "content",
            updatable = false,
            columnDefinition = "TEXT"
    )
    private String content;

    @OneToMany(mappedBy = "idMessage", cascade = CascadeType.ALL, fetch = FetchType.LAZY , orphanRemoval = true)
    private final Set<LikedMessage> likedMessageSet = new HashSet<LikedMessage>();

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

    public Long getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(Long idMessage) {
        this.idMessage = idMessage;
    }

    public Set<LikedMessage> getLikedMessageSet() {
        return likedMessageSet;
    }
}
