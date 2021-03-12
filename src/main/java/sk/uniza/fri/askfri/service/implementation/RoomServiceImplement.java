package sk.uniza.fri.askfri.service.implementation;

import sk.uniza.fri.askfri.dao.IRoomRepository;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.service.IRoomService;

import java.util.List;

public class RoomServiceImplement implements IRoomService {

    private IRoomRepository roomRepository;

    @Override
    public Room saveRoom(Room room) {
        this.roomRepository.save(room);
        return room;
    }

    @Override
    public boolean existsRoomByPasscode(String roomPasscode) {
        return this.roomRepository.existsByRoomPasscode(roomPasscode);
    }

    @Override
    public Room findRoomByPasscode(String roomPasscode) {
        Room existsRoom = this.roomRepository.findByRoomPasscode(roomPasscode);
        if (existsRoom != null) {
            return existsRoom ;
        } else {
            return null;
        }
    }

    @Override
    public List<Room> findAllRooms() {
        return this.roomRepository.findAllByOrderByIdRoomAsc();
    }

    @Override
    public List<Room> findAllUserRooms(User user) {
        return this.roomRepository.findByOwnerOrderByIdRoomDesc(user);
    }
}
