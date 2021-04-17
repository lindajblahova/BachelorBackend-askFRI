package sk.uniza.fri.askfri.model;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "room")
@Table(name = "room")
@NamedEntityGraph(name = "roomMessages",
        attributeNodes = @NamedAttributeNode("messagesSet"))
@NamedEntityGraph(name = "roomQuestions",
        attributeNodes = @NamedAttributeNode("questionSet"))
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_generator")
    @SequenceGenerator(name = "room_generator", sequenceName = "r_id_seq", allocationSize = 1)
    private Long idRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_profile_id_user", referencedColumnName = "idUser", nullable=false, updatable = false)
    private User idOwner;

    @Column(
            name = "room_name",
            updatable = false,
            columnDefinition = "TEXT"
    )
    private String roomName;

    @Column(
            name = "room_passcode",
            updatable = true,
            columnDefinition = "TEXT"
    )
    private String roomPasscode;
    @Column(
            name = "active",
            updatable = true
    )
    private boolean active;

    @OneToMany(mappedBy = "idRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private final Set<Message> messagesSet = new HashSet<Message>();

    @OneToMany(mappedBy = "idRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private final Set<Question> questionSet = new HashSet<Question>();

    public Room() {}

    public Room(User idOwner, String roomName, String roomPasscode, boolean active) {
        this.idOwner = idOwner;
        this.roomName = roomName;
        this.roomPasscode = roomPasscode;
        this.active = active;
    }

    public Long getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(Long idRoom) {
        this.idRoom = idRoom;
    }

    public User getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(User idOwner) {
        this.idOwner = idOwner;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomPasscode() {
        return roomPasscode;
    }

    public void setRoomPasscode(String roomPasscode) {
        this.roomPasscode = roomPasscode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Message> getMessagesSet() {
        return messagesSet;
    }

    public void addMessage(Message message)
    {
        if (!this.messagesSet.contains(message))
        {
            this.messagesSet.add(message);
            message.setIdRoom(this);
        }
    }

    public void removeMessage(Message message)
    {
        if (this.messagesSet.contains(message))
        {
            this.messagesSet.remove(message);
            message.setIdRoom(this);
        }
    }

    public Set<Question> getQuestionSet() {
        return questionSet;
    }

    public void addQuestion(Question question)
    {
        if (!this.questionSet.contains(question))
        {
            this.questionSet.add(question);
            question.setIdRoom(this);
        }
    }

    public void removeQuestion(Question question)
    {
        if (this.questionSet.contains(question))
        {
            this.questionSet.remove(question);
            question.setIdRoom(this);
        }
    }
}
