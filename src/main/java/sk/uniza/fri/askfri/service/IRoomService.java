package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.User;

import java.util.List;

public interface IRoomService {

    Room saveRoom(Room room);
    boolean existsRoomByPasscodeAndActive(String roomPasscode);
    Room findByIdRoom(Long idRoom);
    List<Room> findAllRooms();
    List<Room> findAllUserRooms(User user);
    void deleteRoom(Long idRoom);

}
