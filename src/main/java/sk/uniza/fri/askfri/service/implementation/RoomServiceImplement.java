package sk.uniza.fri.askfri.service.implementation;

import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.IRoomRepository;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.RoomDto;
import sk.uniza.fri.askfri.service.IRoomService;
import sk.uniza.fri.askfri.service.IUserService;

import java.util.List;

@Service
public class RoomServiceImplement implements IRoomService {

    private final IRoomRepository roomRepository;

    public RoomServiceImplement(IRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room saveRoom(Room room) {
        return this.roomRepository.save(room);
    }

    @Override
    public boolean existsRoomByPasscodeAndActive(String roomPasscode) {
        return this.roomRepository.existsByRoomPasscodeAndActive(roomPasscode, true);
    }

    @Override
    public Room findRoomByPasscodeAndActive(String roomPasscode) {
        return this.roomRepository.findByRoomPasscodeAndActive(roomPasscode,true);
    }

    @Override
    public Room findByIdRoom(Long idRoom) {
        Room existsRoom = this.roomRepository.findRoomByIdRoom(idRoom);
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
        return this.roomRepository.findByIdOwnerOrderByIdRoomDesc(user);
    }

    @Override
    public void deleteRoom(Long idRoom) {
        this.roomRepository.deleteById(idRoom);
    }

}
