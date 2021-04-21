package sk.uniza.fri.askfri.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.IRoomRepository;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.ResponseDto;
import sk.uniza.fri.askfri.model.dto.RoomDto;
import sk.uniza.fri.askfri.service.*;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoomServiceImplement implements IRoomService {

    private final IRoomRepository roomRepository;
    private final IUserService userService;
    private final ModelMapper modelMapper;

    public RoomServiceImplement(IRoomRepository roomRepository, IUserService userService, ModelMapper modelMapper) {
        this.roomRepository = roomRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseDto createRoom(RoomDto roomDto) {

        Room newRoom = modelMapper.map(roomDto, Room.class);
        User roomOwner = this.userService.findUserByIdUser(roomDto.getIdOwner());
        if (roomOwner != null && !roomDto.getRoomPasscode().trim().equals("") &&
                !roomDto.getRoomName().trim().equals(""))
        {
            roomOwner.addRoom(newRoom);
            this.userService.saveUser(roomOwner);
            return new ResponseDto(newRoom.getIdRoom(),
                    "Miestnosť " + newRoom.getRoomName() +  " bola vytvorená");
        }
        throw new NullPointerException("Nebolo možné vytvoriť miestnosť");
    }

    @Override
    public Room saveRoom(Room room) {
       return this.roomRepository.save(room);
    }

    @Override
    public Room saveRoomNew(Room room) {
        return this.roomRepository.saveAndFlush(room);
    }

    @Override
    public boolean existsRoomByPasscodeAndActive(String roomPasscode) {
        return this.roomRepository.existsByRoomPasscodeAndActive(roomPasscode, true);
    }

    @Override
    public RoomDto findRoomByPasscodeAndActive(String roomPasscode) {
        Room room = this.roomRepository.findByRoomPasscodeAndActive(roomPasscode,true);
        if (room != null)
        {
            return this.modelMapper.map(room, RoomDto.class);
        }
        throw new NullPointerException("Miestnosť nebola nájdená");
    }

    @Override
    public ResponseDto updateRoomPasscode(RoomDto roomDto) {
        Room foundRoom = this.roomRepository.findRoomByIdRoom(roomDto.getIdRoom());
        User owner = this.userService.findUserByIdUser(foundRoom.getIdOwner().getIdUser());
        if (foundRoom != null && owner != null && !roomDto.getRoomPasscode().trim().equals("") &&
                (!this.roomRepository.existsByRoomPasscodeAndActive(roomDto.getRoomPasscode(), true))) {
            owner.removeRoom(foundRoom);
            foundRoom.setRoomPasscode(roomDto.getRoomPasscode());
            foundRoom.setActive(true);
            owner.addRoom(foundRoom);
            this.userService.saveUser(owner);
            return new ResponseDto(roomDto.getIdRoom(),
                    "Nový kód je " + foundRoom.getRoomPasscode());
        }
        throw new IllegalArgumentException("Prísupový kód nebolo možné upraviť");
    }

    @Override
    public ResponseDto updateRoomActivity(Long idRoom) {
        Room foundRoom = this.roomRepository.findRoomByIdRoom(idRoom);
        User owner = this.userService.findUserByIdUser(foundRoom.getIdOwner().getIdUser());
        if (foundRoom != null && owner != null) {
            owner.removeRoom(foundRoom);
            foundRoom.setActive(!foundRoom.isActive());
            owner.addRoom(foundRoom);
            this.userService.saveUser(owner);
            return new ResponseDto(idRoom,
                    foundRoom.isActive() ? "Miestnosť je aktívna" : "Miestnosť je neaktívna");
        }
        throw new NullPointerException("Miestnosť nebola nájdená");
    }

    @Override
    public RoomDto findRoomDtoByIdRoom(Long idRoom) {
        Room existsRoom = this.roomRepository.findRoomByIdRoom(idRoom);
        if (existsRoom != null) {
            return this.modelMapper.map(existsRoom, RoomDto.class);
        }
        throw new NullPointerException("Miestnosť nebola nájdená");
    }

    @Override
    public Room findByIdRoom(Long idRoom) {
        return this.roomRepository.findRoomByIdRoom(idRoom);
    }

    @Override
    public Set<RoomDto> findAllRooms() {
        return this.roomRepository.findAllByOrderByIdRoomAsc()
                .stream()
                .map( room -> modelMapper.map(room, RoomDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public ResponseDto deleteRoom(Long idRoom) {
        Room foundRoom = this.roomRepository.findRoomByIdRoom(idRoom);
        User owner = foundRoom.getIdOwner();
        owner.removeRoom(foundRoom);
        this.userService.saveUser(owner);
        return new ResponseDto(idRoom, "Miestnosť bola vymazaná");
    }

    @Override
    public Set<RoomDto> findUserRooms(Long idUser) {
        User roomOwner = this.userService.findUserByIdUser(idUser);
        if (roomOwner != null)
        {
            return roomOwner.getRoomSet()
                    .stream()
                    .map(room -> modelMapper.map(room, RoomDto.class))
                    .collect(Collectors.toSet());
        }
        throw new NullPointerException("Používateľ nebol nájdený");
    }

}
