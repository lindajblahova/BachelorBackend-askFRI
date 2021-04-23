package sk.uniza.fri.askfri.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/** Trieda mapovana na tabulku message databazy, sluzi pre zaznamenanie sprav
 *  pre miestnost
 *  obsahuje ID spravy, rodicovsku miestnost a obsah spravy
 *  ID je primarnym klucom, ktory je generovany sekvenciou m_id_seq
 *  Message je vo vztahu ManyToOne k rodicovskej miestnosti, referencuje ID miestnosti
 *  Message je vo vztahu OneToMany k LikedMessage ako rodicovska sprava
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@Entity(name = "message")
@Table(name = "message")
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
