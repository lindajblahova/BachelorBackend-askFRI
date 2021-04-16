package sk.uniza.fri.askfri.api;

import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.ResponseDto;
import sk.uniza.fri.askfri.model.dto.RoomDto;
import sk.uniza.fri.askfri.service.IMessageService;
import sk.uniza.fri.askfri.service.IQuestionService;
import sk.uniza.fri.askfri.service.IRoomService;
import sk.uniza.fri.askfri.service.IUserService;

import javax.persistence.EntityManager;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/rooms")
public class RoomController {

    private final IRoomService roomService;
    private final IUserService userService;
    private final IQuestionService questionService;
    private final IMessageService messageService;
    private final ModelMapper modelMapper;

    public RoomController(IRoomService roomService, IUserService userService, IQuestionService questionService, IMessageService messageService, ModelMapper modelMapper) {
        this.roomService = roomService;
        this.userService = userService;
        this.questionService = questionService;
        this.messageService = messageService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/get")
    public ResponseEntity<Set<RoomDto>> getAllRooms() {
        return new ResponseEntity<Set<RoomDto>>(this.roomService.findAllRooms()
                .stream()
                .map( room -> modelMapper.map(room, RoomDto.class))
                .collect(Collectors.toSet()), HttpStatus.OK);
    }

    @GetMapping("/get/room/{id}")
    public ResponseEntity<RoomDto> getRoom(@PathVariable("id") long id) {
        Room room = this.roomService.findByIdRoom(id);
        if (room != null)
        {
            return new ResponseEntity<RoomDto>(this.modelMapper.map(room, RoomDto.class), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/get/user/{id}")
    public ResponseEntity<Set<RoomDto>> getAllUserRooms(@PathVariable("id") Long idUser) {
        User roomOwner = this.userService.getUserByIdUser(idUser);
        if (roomOwner != null)
        {
            return new ResponseEntity<Set<RoomDto>>(roomOwner.getRoomSet()
                    .stream()
                    .map(room -> modelMapper.map(room, RoomDto.class))
                    .collect(Collectors.toSet()), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<ResponseDto> createRoom(@RequestBody RoomDto roomDto) {
        Room newRoom = modelMapper.map(roomDto, Room.class);
        User roomOwner = this.userService.getUserByIdUser(roomDto.getIdOwner());
        if (this.roomService.existsRoomByPasscodeAndActive(roomDto.getRoomPasscode())) {
            return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
        }
        if (roomOwner != null && !roomDto.getRoomPasscode().equals("") &&
            !roomDto.getRoomName().equals(""))
        {
            newRoom.setIdOwner(roomOwner);
            newRoom = roomService.saveRoom(newRoom);
            roomOwner.getRoomSet().add(newRoom); // TODO
            return new ResponseEntity<ResponseDto>(new ResponseDto(newRoom.getIdRoom(),
                    "Miestnosť " + newRoom.getRoomName() +  " bola vytvorená"),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/get/passcode/{passcode}")
    public boolean isPasscodeCurrentlyUsed(@PathVariable("passcode") String passcode) {
        Room foundRoom = this.roomService.findRoomByPasscodeAndActive(passcode);
        return foundRoom != null;
    }

    @GetMapping(value = "/get/room-passcode/{passcode}")
    public ResponseEntity<RoomDto> getActiveRoomByPasscode(@PathVariable("passcode") String passcode) {
        Room foundRoom = this.roomService.findRoomByPasscodeAndActive(passcode);
        if (foundRoom != null)
        {
            return new ResponseEntity<>(this.modelMapper.map(foundRoom, RoomDto.class),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/update/passcode")
    public ResponseEntity<ResponseDto> updateRoomPasscode(@RequestBody RoomDto roomDto) {
        Room foundRoom = this.roomService.findByIdRoom(roomDto.getIdRoom());
        if (foundRoom != null  && !roomDto.getRoomPasscode().equals("")) {
            foundRoom.setRoomPasscode(roomDto.getRoomPasscode());
            foundRoom.setActive(true);
            foundRoom = this.roomService.saveRoom(foundRoom);
            return new ResponseEntity<>(new ResponseDto(roomDto.getIdRoom(),
                    "Nový kód je " + foundRoom.getRoomPasscode()),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
    }

    @PutMapping(value = "/update/activity")
    public ResponseEntity<ResponseDto> updateRoomActivity(@RequestBody long idRoom) {
        Room foundRoom = this.roomService.findByIdRoom(idRoom);
        if (foundRoom.getIdRoom() != null) {
            foundRoom.setActive(!foundRoom.isActive());
            foundRoom = this.roomService.saveRoom(foundRoom);
            return new ResponseEntity<>(new ResponseDto(idRoom,
                    foundRoom.isActive() ? "Miestnosť je aktívna" : "Miestnosť je neaktívna"),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<ResponseDto> deleteRoom(@PathVariable("id") long idRoom) {
        try {
            Room foundRoom = this.roomService.findByIdRoom(idRoom);
            User owner = foundRoom.getIdOwner();
            owner.getRoomSet().remove(foundRoom);
            this.roomService.deleteRoom(idRoom);
            return new ResponseEntity<>(new ResponseDto(idRoom, "Miestnosť bola vymazaná"),HttpStatus.OK);
        } catch (EmptyResultDataAccessException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }
}
