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

    private IRoomRepository roomRepository;
    private IUserService userService;

    public RoomServiceImplement(IRoomRepository roomRepository, IUserService userService) {
        this.roomRepository = roomRepository;
        this.userService = userService;
    }

    @Override
    public Room saveRoom(Room room) {
        return this.roomRepository.save(room);
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
        return this.roomRepository.findByIdOwnerOrderByIdRoomDesc(user);
    }
}
