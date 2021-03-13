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
                .map( room -> {
                    RoomDto mappedRoom =  modelMapper.map(room, RoomDto.class);
                    mappedRoom.setUserEmail(this.userService.getUserByIdUser(room.getIdOwner()).getEmail());
                    return mappedRoom;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/get/user")
    public List<RoomDto> getAllUserRooms(@RequestBody String email) {
        User roomOwner = this.userService.getUserByEmail(email);
        return this.roomService.findAllUserRooms(roomOwner)
                .stream()
                .map( room -> {
                    RoomDto mappedRoom =  modelMapper.map(room, RoomDto.class);
                    mappedRoom.setUserEmail(this.userService.getUserByIdUser(room.getIdOwner()).getEmail());
                    return mappedRoom;
                })
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/add")
    public ResponseEntity createRoom(@RequestBody RoomDto roomDto) {
        User roomOwner = this.userService.getUserByEmail(roomDto.getUserEmail());
        Room newRoom = modelMapper.map(roomDto, Room.class);
        if (this.roomService.existsRoomByPasscodeAndActive(roomDto.getRoomPasscode())) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
        newRoom.setIdOwner(roomOwner);
        roomService.saveRoom(newRoom);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping(value = "/update/passcode")
    public ResponseEntity updateRoomPasscode(@RequestBody RoomDto roomDto) {
        Room foundRoom = this.roomService.findByIdRoom(roomDto.getIdRoom());
        if (foundRoom.getIdRoom() != null) {
            foundRoom.setRoomPasscode(roomDto.getRoomPasscode());
            foundRoom.setActive(true);
            this.roomService.saveRoom(foundRoom);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/update/inactive")
    public ResponseEntity updateRoomActivity(@RequestBody Long idRoom) {
        Room foundRoom = this.roomService.findByIdRoom(idRoom);
        if (foundRoom.getIdRoom() != null) {
            foundRoom.setActive(!foundRoom.isActive());
            this.roomService.saveRoom(foundRoom);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
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
    public ResponseEntity deleteRoom(@RequestBody Long idRoom) {
        this.roomService.deleteRoom(idRoom);
        return new ResponseEntity(HttpStatus.OK);
    }
}
