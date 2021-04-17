package sk.uniza.fri.askfri.api;

import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.model.LikedMessage;
import sk.uniza.fri.askfri.model.Message;
import sk.uniza.fri.askfri.model.Question;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.dto.*;
import sk.uniza.fri.askfri.service.IMessageService;
import sk.uniza.fri.askfri.service.IRoomService;

import java.util.HashSet;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
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
    public ResponseEntity<ResponseDto> createMessage(@RequestBody MessageDto messageDto) {
        Room parentRoom = this.roomService.findByIdRoom(messageDto.getIdRoom());
        Message message = modelMapper.map(messageDto, Message.class);
        if (parentRoom != null && !messageDto.getContent().equals("")) {
            parentRoom.addMessage(message);
            this.roomService.saveRoom(parentRoom);
            return new ResponseEntity<ResponseDto>(new ResponseDto(message.getIdMessage(), "Správa bola vytvorená"), HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping(value = "/room/{id}")
    public ResponseEntity<Set<MessageWithLikes>> getAllRoomMessages(@PathVariable("id") long idRoom) {
        Room parentRoom = this.roomService.findByIdRoom(idRoom);
        if (parentRoom != null)
        {
            Set<MessageWithLikes> setMessageWithLikes = new HashSet<MessageWithLikes>();
            Set<Message> setMessages = parentRoom.getMessagesSet();
            setMessages.forEach(message ->
                    {
                        Set<LikedMessage> likedMessageSet = message.getLikedMessageSet();
                        Set<LikeMesClass> dtoSet = new HashSet<LikeMesClass>();
                        if (likedMessageSet != null) {
                            likedMessageSet.forEach(data -> dtoSet.add(new LikeMesClass(data.getIdUser().getIdUser(),
                                    data.getIdMessage().getIdMessage())));
                            setMessageWithLikes.add(new MessageWithLikes(
                                    this.modelMapper.map(message, MessageDto.class),
                                    dtoSet/*.stream()
                                            .map( data -> this.modelMapper.map(data, LikedMessageDto.class))
                                            .collect(Collectors.toSet())*/));
                        }
                    });
                    return new ResponseEntity<>(setMessageWithLikes, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<ResponseDto> deleteMessage(@PathVariable("id") long idMessage) {
        try {
            Message message = this.messageService.findByIdMessage(idMessage);
            Room parentRoom = this.roomService.findByIdRoom(message.getIdRoom());
            parentRoom.removeMessage(message);
            this.roomService.saveRoom(parentRoom);
            return new ResponseEntity<ResponseDto>(new ResponseDto(idMessage,
                    "Správa bola vymazaná"),HttpStatus.OK);
        } catch (EmptyResultDataAccessException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }


    /*@GetMapping(value = "/message/likes/{id}")
    public ResponseEntity<Set<LikedMessageDto>> getAllMessageLikes(@PathVariable("id") Long idMessage) {
        Message parentMessage = this.messageService.findByIdMessage(idMessage);
        Set<LikedMessage> likedMessageSet = parentMessage.getLikedMessageSet();
        Set<LikeMesClass> dtoSet = new HashSet<LikeMesClass>();
        if (likedMessageSet != null) {
            likedMessageSet.forEach(data -> dtoSet.add(new LikeMesClass(data.getIdUser().getIdUser(),
                    data.getIdMessage().getIdMessage())));
            return new ResponseEntity<Set<LikedMessageDto>>(dtoSet.stream().map(data -> this.modelMapper.map(data,
                    LikedMessageDto.class)).collect(Collectors.toSet()), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }*/
}
