package sk.uniza.fri.askfri.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "message")
@Table(name = "message")
@NamedEntityGraph(name = "messageLikes",
        attributeNodes = @NamedAttributeNode("likedMessageSet"))
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_generator")
    @SequenceGenerator(name = "message_generator", sequenceName = "m_id_seq", allocationSize = 1)
    @Column(nullable = false, updatable = false)
    private Long idMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_id_room", referencedColumnName = "idRoom", nullable=false)
    private Room idRoom;

    @Column(
            name = "content",
            updatable = false,
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String content;

    @OneToMany(mappedBy = "idMessage",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY , orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<LikedMessage> likedMessageSet = new HashSet<LikedMessage>();

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

    public void removeAllLikedMessageSet() {
        Set<LikedMessage> set2 = this.likedMessageSet;
        this.likedMessageSet.removeAll(set2);
    }

    public void addLikedMessage(LikedMessage likedMessage)
    {
        if (!this.likedMessageSet.contains(likedMessage))
        {
            this.likedMessageSet.add(likedMessage);
            likedMessage.setIdMessage(this);
        }
    }

    public void removeLikedMessage(LikedMessage likedMessage)
    {
        if (this.likedMessageSet.contains(likedMessage))
        {
            this.likedMessageSet.remove(likedMessage);
            likedMessage.setIdMessage(null);
        }
    }
}
