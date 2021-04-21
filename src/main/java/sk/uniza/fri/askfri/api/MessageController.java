package sk.uniza.fri.askfri.api;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.model.dto.*;
import sk.uniza.fri.askfri.service.IMessageService;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/messages")
public class MessageController {

    private final IMessageService messageService;

    public MessageController(IMessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<ResponseDto> createMessage(@RequestBody MessageDto messageDto) {
        try {
            ResponseDto dto = this.messageService.createMessage(messageDto);
            return new ResponseEntity<ResponseDto>(dto, HttpStatus.OK);
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping(value = "/room/{id}")
    public ResponseEntity<Set<MessageWithLikes>> getAllRoomMessages(@PathVariable("id") Long idRoom) {
        try {
            return new ResponseEntity<Set<MessageWithLikes>>(this.messageService.getAllRoomMessages(idRoom), HttpStatus.OK);
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<ResponseDto> deleteMessage(@PathVariable("id") Long idMessage) {
        try {
            ResponseDto dto = this.messageService.deleteMessage(idMessage);
            return new ResponseEntity<ResponseDto>(dto,HttpStatus.OK);
        } catch (EmptyResultDataAccessException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }
}
