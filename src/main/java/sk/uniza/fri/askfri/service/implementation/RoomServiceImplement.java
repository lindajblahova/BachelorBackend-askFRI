package sk.uniza.fri.askfri.service.implementation;

import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.IMessageRepository;
import sk.uniza.fri.askfri.dao.IRoomRepository;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.service.IRoomService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
public class RoomServiceImplement implements IRoomService {

    private final IRoomRepository roomRepository;
    private final IMessageRepository messageRepository;

    public RoomServiceImplement(IRoomRepository roomRepository, IMessageRepository messageRepository) {
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
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
    public Set<Room> findAllRooms() {
        return this.roomRepository.findAllByOrderByIdRoomAsc();
    }

    @Override
    public void deleteRoom(Long idRoom) {
        this.roomRepository.deleteById(idRoom);
    }

//    @Transactional
//    @Override
//    public void deleteUserRooms(Long idUser) {
//        this.roomRepository.deleteRoomsByIdOwner_IdUser(idUser);
//    }

}
