package sk.uniza.fri.askfri.api;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.RoomDto;
import sk.uniza.fri.askfri.model.dto.UserDto;
import sk.uniza.fri.askfri.service.IRoomService;
import sk.uniza.fri.askfri.service.IUserService;

import java.lang.reflect.Type;
import java.sql.SQLOutput;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/rooms")
public class RoomController {

    private final IRoomService roomService;
    private final IUserService userService;
    private final ModelMapper modelMapper;

    public RoomController(IRoomService roomService, IUserService userService, ModelMapper modelMapper) {
        this.roomService = roomService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/get")
    public List<RoomDto> getAllRooms() {
        return this.roomService.findAllRooms()
                .stream()
                .map( room -> modelMapper.map(room, RoomDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/get/room/{id}")
    public RoomDto getRoom(@PathVariable("id") long id) {
        Room room = this.roomService.findByIdRoom(id);
        return this.modelMapper.map(room, RoomDto.class);
    }

    @GetMapping("/get/user/{id}")
    public List<RoomDto> getAllUserRooms(@PathVariable("id") long idUser) {
        User roomOwner = this.userService.getUserByIdUser(idUser);
        return this.roomService.findAllUserRooms(roomOwner)
                .stream()
                .map( room -> modelMapper.map(room, RoomDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/add")
    public ResponseEntity<RoomDto> createRoom(@RequestBody RoomDto roomDto) {
        Room newRoom = modelMapper.map(roomDto, Room.class);
        User roomOwner = this.userService.getUserByIdUser(roomDto.getIdOwner());
        if (this.roomService.existsRoomByPasscodeAndActive(roomDto.getRoomPasscode())) {
            return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
        }
        if (roomOwner != null)
        {
            newRoom.setIdOwner(roomOwner);
            Room room = roomService.saveRoom(newRoom);
            return new ResponseEntity<>(modelMapper.map(room, RoomDto.class),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/get/passcode/{passcode}")
    public boolean isPasscodeCurrentlyUsed(@PathVariable("passcode") String passcode) {
        Room foundRoom = this.roomService.findRoomByPasscodeAndActive(passcode);
        return foundRoom != null;
    }

    @GetMapping(value = "/get/room-passcode/{passcode}")
    public ResponseEntity<RoomDto>  getActiveRoomByPasscode(@PathVariable("passcode") String passcode) {
        Room foundRoom = this.roomService.findRoomByPasscodeAndActive(passcode);
        if (foundRoom != null)
        {
            return new ResponseEntity<>(this.modelMapper.map(foundRoom, RoomDto.class),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/update/passcode")
    public ResponseEntity<RoomDto> updateRoomPasscode(@RequestBody RoomDto roomDto) {
        Room foundRoom = this.roomService.findByIdRoom(roomDto.getIdRoom());
        if (foundRoom.getIdRoom() != null) {
            foundRoom.setRoomPasscode(roomDto.getRoomPasscode());
            foundRoom.setActive(true);
            this.roomService.saveRoom(foundRoom);
            return new ResponseEntity<>(roomDto,HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/update/activity")
    public ResponseEntity<RoomDto> updateRoomActivity(@RequestBody long idRoom) {
        Room foundRoom = this.roomService.findByIdRoom(idRoom);
        if (foundRoom.getIdRoom() != null) {
            foundRoom.setActive(!foundRoom.isActive());
            this.roomService.saveRoom(foundRoom);
            RoomDto room = this.modelMapper.map(foundRoom,RoomDto.class);
            return new ResponseEntity<>(room,HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Room> deleteRoom(@PathVariable("id") long idRoom) {
        this.roomService.deleteRoom(idRoom);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }
}
