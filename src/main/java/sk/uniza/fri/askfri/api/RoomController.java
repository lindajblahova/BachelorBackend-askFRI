package sk.uniza.fri.askfri.api;

import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.RoomDto;
import sk.uniza.fri.askfri.service.IRoomService;
import sk.uniza.fri.askfri.service.IUserService;

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
    public void method() {
        System.out.println("som");
    }

    @PostMapping(value = "/add")
    public ResponseEntity createRoom(@RequestBody RoomDto roomDto) {
        System.out.println(roomDto.getUserEmail());
        System.out.println(roomDto.getRoomName());
        System.out.println(roomDto.getRoomPasscode());
        User roomOwner = this.userService.getUser(roomDto.getUserEmail());
        Room newRoom = modelMapper.map(roomDto, Room.class);
        if (roomService.existsRoomByPasscode(roomDto.getRoomPasscode())) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
        newRoom.setIdOwner(roomOwner);
        System.out.println("New passcode: " + newRoom.getRoomPasscode());
        System.out.println("New roomname: " + newRoom.getRoomName());
        System.out.println("New owner: " + newRoom.getIdOwner());
        roomService.saveRoom(newRoom);
        return new ResponseEntity(HttpStatus.OK);
    }
}
