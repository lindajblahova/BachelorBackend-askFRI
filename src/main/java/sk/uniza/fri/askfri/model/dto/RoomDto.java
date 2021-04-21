package sk.uniza.fri.askfri.model.dto;

public class RoomDto {

    private Long idRoom;
    private Long idOwner;
    private String roomName;
    private String roomPasscode;
    private boolean active = true;

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

    public Long getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(Long idRoom) {
        this.idRoom = idRoom;
    }

    public Long getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(Long idUser) {
        this.idOwner = idUser;
    }
}
