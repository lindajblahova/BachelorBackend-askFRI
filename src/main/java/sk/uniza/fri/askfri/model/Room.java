package sk.uniza.fri.askfri.model;


import javax.persistence.*;
import java.util.Set;

@Entity(name = "room")
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idRoom;

    @ManyToOne
    @JoinColumn(name="id_user", nullable=false)
    private User owner;

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


    public Room() {}

    public Room(User owner, String roomName, String roomPasscode, boolean active) {
        this.owner = owner;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User idOwner) {
        this.owner = idOwner;
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
}
