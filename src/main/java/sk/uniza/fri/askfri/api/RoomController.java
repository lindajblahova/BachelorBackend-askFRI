package sk.uniza.fri.askfri.api;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.ResponseDto;
import sk.uniza.fri.askfri.model.dto.RoomDto;
import sk.uniza.fri.askfri.service.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/rooms")
public class RoomController {

    private final IRoomService roomService;

    private final ILikedMessageService likedMessageService;
    private final IAnsweredQuestionService answeredQuestionService;
    private final IAnswerService answerService;
    private final IQuestionService questionService;
    private final IMessageService messageService;
    private final IUserService userService;

    public RoomController(IRoomService roomService, ILikedMessageService likedMessageService,
                          IAnsweredQuestionService answeredQuestionService, IAnswerService answerService,
                          IQuestionService questionService, IMessageService messageService, IUserService userService) {
        this.roomService = roomService;
        this.likedMessageService = likedMessageService;
        this.answeredQuestionService = answeredQuestionService;
        this.answerService = answerService;
        this.questionService = questionService;
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping("/get/room/{id}")
    public ResponseEntity<RoomDto> getRoom(@PathVariable("id") long id) {
        try {
            RoomDto room = this.roomService.findRoomDtoByIdRoom(id);
            return new ResponseEntity<RoomDto>(room, HttpStatus.OK);
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/get/user/{id}")
    public ResponseEntity<Set<RoomDto>> getAllUserRooms(@PathVariable("id") Long idUser) {
        try {
            return new ResponseEntity<Set<RoomDto>>(this.roomService.findUserRooms(idUser), HttpStatus.OK);
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/add")
    public ResponseEntity<ResponseDto> createRoom(@RequestBody RoomDto roomDto) {
        try {
            if (this.roomService.existsRoomByPasscodeAndActive(roomDto.getRoomPasscode())) {
                return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);// TODO over status kód angular
            }
            ResponseDto responseDto = this.roomService.createRoom(roomDto);
            return new ResponseEntity<ResponseDto>(responseDto, HttpStatus.OK);
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping(value = "/get/passcode/{passcode}")
    public boolean isPasscodeCurrentlyUsed(@PathVariable("passcode") String passcode) {
       return this.roomService.existsRoomByPasscodeAndActive(passcode);
    }

    @GetMapping(value = "/get/room-passcode/{passcode}")
    public ResponseEntity<RoomDto> getActiveRoomByPasscode(@PathVariable("passcode") String passcode) {
        try {
            RoomDto roomDto = this.roomService.findRoomByPasscodeAndActive(passcode);
            return new ResponseEntity<>(roomDto, HttpStatus.OK);
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update/passcode")
    public ResponseEntity<ResponseDto> updateRoomPasscode(@RequestBody RoomDto roomDto) {
        try {
            ResponseDto responseDto = this.roomService.updateRoomPasscode(roomDto);
            return new ResponseEntity<>(responseDto,HttpStatus.OK);
        } catch (IllegalArgumentException e ) {
            return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping(value = "/update/activity")
    public ResponseEntity<ResponseDto> updateRoomActivity(@RequestBody Long idRoom) {
        try {
            ResponseDto responseDto = this.roomService.updateRoomActivity(idRoom);
            return new ResponseEntity<>(responseDto,HttpStatus.OK);
        } catch (NullPointerException e ) {
            return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<ResponseDto> deleteRoom(@PathVariable("id") Long idRoom) {
        try {
            ResponseDto responseDto = this.roomService.deleteRoom(idRoom);
            return new ResponseEntity<>(responseDto,HttpStatus.OK);
        } catch (EmptyResultDataAccessException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }
}
