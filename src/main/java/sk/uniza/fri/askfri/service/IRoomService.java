package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.Room;
import java.util.Set;

public interface IRoomService {

    Room saveRoom(Room room);
    boolean existsRoomByPasscodeAndActive(String roomPasscode);
    Room findRoomByPasscodeAndActive(String roomPasscode);
    Room findByIdRoom(Long idRoom);
    Set<Room> findAllRooms();
    void deleteRoom(Long idRoom);

  //  void deleteUserRooms(Long idUser);

}
