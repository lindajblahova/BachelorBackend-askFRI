package sk.uniza.fri.askfri.api;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.model.Message;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.dto.MessageDto;
import sk.uniza.fri.askfri.service.IMessageService;
import sk.uniza.fri.askfri.service.IRoomService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final IMessageService messageService;
    private final IRoomService roomService;
    private final ModelMapper modelMapper;

    public MessageController(IMessageService messageService, IRoomService roomService, ModelMapper modelMapper) {
        this.messageService = messageService;
        this.roomService = roomService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/add")
    public ResponseEntity createMessage(@RequestBody MessageDto messageDto) {
        Room parentRoom = this.roomService.findByIdRoom(messageDto.getIdRoom());
        Message message = modelMapper.map(messageDto, Message.class);
        if (parentRoom != null) {
            this.messageService.saveMessage(message);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/room")
    public List<MessageDto> getAllRoomMessages(@RequestBody Long idRoom) {
        Room parentRoom = this.roomService.findByIdRoom(idRoom);
        return this.messageService.findAllRoomMessages(parentRoom)
                .stream()
                .map( message ->  modelMapper.map(message, MessageDto.class))
                .collect(Collectors.toList());
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity deleteMessage(@RequestBody Long idMessage) {
        this.messageService.deleteMessage(idMessage);
        return new ResponseEntity(HttpStatus.OK);
    }
}
