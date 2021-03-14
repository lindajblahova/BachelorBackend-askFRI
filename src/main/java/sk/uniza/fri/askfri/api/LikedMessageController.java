package sk.uniza.fri.askfri.api;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.model.*;
import sk.uniza.fri.askfri.model.dto.AnsweredQuestionDto;
import sk.uniza.fri.askfri.model.dto.LikedMessageDto;
import sk.uniza.fri.askfri.service.ILikedMessageService;
import sk.uniza.fri.askfri.service.IMessageService;
import sk.uniza.fri.askfri.service.IUserService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/liked")
public class LikedMessageController {

    private final ModelMapper modelMapper;
    private final IUserService userService;
    private final IMessageService messageService;
    private final ILikedMessageService likedMessageService;

    public LikedMessageController(ModelMapper modelMapper, IUserService userService,
                                  IMessageService messageService,
                                  ILikedMessageService likedMessageService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.messageService = messageService;
        this.likedMessageService = likedMessageService;
    }

    @PostMapping(value = "/add")
    public ResponseEntity createLikedMessage(@RequestBody LikedMessageDto likedMessageDto) {
        Message parentMessage = this.messageService.findByIdMessage(likedMessageDto.getIdMessage());
        User parentUser = this.userService.getUserByIdUser(likedMessageDto.getIdUser());
        LikedMessage likedMessage = modelMapper.map(likedMessageDto, LikedMessage.class);
        if (parentMessage != null && parentUser != null) {
            this.likedMessageService.saveMessageLike(likedMessage);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/message")
    public Integer getQuestionUsersAnsweredCount(@RequestBody Long idMessage) {
        Message parentMessage = this.messageService.findByIdMessage(idMessage);
        return this.likedMessageService.messageLikesCount(parentMessage.getIdMessage());
    }

    @GetMapping(value = "/user")
    public List<Integer> getAllQuestionAnswers(@RequestBody Long idUser) {
        User parentUser = this.userService.getUserByIdUser(idUser);
        return this.likedMessageService.userLikedMessages(parentUser.getIdUser());
        //return parentUser.getLikedMessageSet();
    }

    //TODO: delete like
    @DeleteMapping(value = "/delete")
    public ResponseEntity deleteLikeFromMessage(@RequestBody Long idLikedMessage){
        this.likedMessageService.deleteMessage(idLikedMessage);
        return new ResponseEntity(HttpStatus.OK);
    }
}
