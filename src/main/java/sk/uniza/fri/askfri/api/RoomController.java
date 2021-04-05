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

    @PostMapping("/get/user")
    public List<RoomDto> getAllUserRooms(@RequestBody Long id) {
        User roomOwner = this.userService.getUserByIdUser(id);
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
            RoomDto returnRoom = modelMapper.map(newRoom, RoomDto.class);
            roomService.saveRoom(newRoom);
            return new ResponseEntity<>(roomDto,HttpStatus.OK);
        }
        return new ResponseEntity<>(roomDto,HttpStatus.BAD_REQUEST);
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

    @PutMapping(value = "/update/inactive")
    public ResponseEntity<RoomDto> updateRoomActivity(@RequestBody Long idRoom) {
        Room foundRoom = this.roomService.findByIdRoom(idRoom);
        if (foundRoom.getIdRoom() != null) {
            foundRoom.setActive(!foundRoom.isActive());
            this.roomService.saveRoom(foundRoom);
            RoomDto room = this.modelMapper.map(foundRoom,RoomDto.class);
            return new ResponseEntity<>(room,HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    /*@PutMapping(value = "/update/active")
    public ResponseEntity updateRoomA(@RequestBody RoomDto roomDto) {
        Room foundRoom = this.roomService.findByIdRoom(roomDto.getIdRoom());
        if (foundRoom.getIdRoom() != null) {
            if (this.roomService.existsRoomByPasscodeAndActive(foundRoom.getRoomPasscode())) {
                foundRoom.setRoomPasscode(roomDto.getRoomPasscode());
                foundRoom.setActive(true);
            } else {
                foundRoom.setActive(true);
            }
            this.roomService.saveRoom(foundRoom);
            return new ResponseEntity(HttpStatus.OK);
            }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }*/


    @DeleteMapping(value = "/delete")
    public ResponseEntity<RoomDto> deleteRoom(@RequestBody Long idRoom) {
        this.roomService.deleteRoom(idRoom);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }
}
