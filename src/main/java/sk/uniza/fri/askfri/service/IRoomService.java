package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.dto.ResponseDto;
import sk.uniza.fri.askfri.model.dto.RoomDto;

import java.util.Set;

public interface IRoomService {

    ResponseDto createRoom(RoomDto roomDto);
    Room saveRoom(Room room);
    Room saveRoomNew(Room room);
    boolean existsRoomByPasscodeAndActive(String roomPasscode);
    RoomDto findRoomByPasscodeAndActive(String roomPasscode);
    ResponseDto updateRoomPasscode(RoomDto roomDto);
    ResponseDto updateRoomActivity(Long idRoom);
    RoomDto findRoomDtoByIdRoom(Long idRoom);
    Room findByIdRoom(Long idRoom);
    Set<RoomDto> findAllRooms();
    ResponseDto deleteRoom(Long idRoom);

    Set<RoomDto> findUserRooms(Long idUser);

}
