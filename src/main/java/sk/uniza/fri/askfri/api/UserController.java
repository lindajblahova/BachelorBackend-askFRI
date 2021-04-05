package sk.uniza.fri.askfri.api;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
;
import sk.uniza.fri.askfri.model.*;
import sk.uniza.fri.askfri.model.dto.AnsweredQuestionDto;
import sk.uniza.fri.askfri.model.dto.LikedMessageDto;
import sk.uniza.fri.askfri.model.dto.UserDto;
import sk.uniza.fri.askfri.service.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")

public class UserController {

    private final IUserService userService;
    private final IMessageService messageService;
    private final ModelMapper modelMapper;
    private final IQuestionService questionService;

    public UserController(IUserService userService, IMessageService messageService, ModelMapper modelMapper, IQuestionService questionService) {
        this.userService = userService;
        this.messageService = messageService;
        this.modelMapper = modelMapper;
        this.questionService = questionService;
    }

    @GetMapping(value = "/all")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map( user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/user")
    public ResponseEntity<User> getUser(@RequestBody String email) {
        boolean userExists = this.userService.existsByEmail(email);
        if (userExists) {
            return new ResponseEntity<>(this.userService.getUserByEmail(email),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST); // TODO exception
    }

    @PutMapping(value = "/update")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        User foundUser = this.userService.getUserByEmail(userDto.getEmail());
        if (foundUser.getIdUser() != null) {
            foundUser.setPassword(userDto.getPassword());
            this.userService.saveUser(foundUser);
            return new ResponseEntity<>(userDto,HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<User> deleteUser(@RequestBody String email) {
        User roomsOwner = this.userService.getUserByEmail(email);
        this.userService.deleteUser(roomsOwner.getIdUser());
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    //--------------------------------------------------------------------------------------
    //                                  MESSAGE LIKE
    //--------------------------------------------------------------------------------------

    @PostMapping(value = "/user/message/like")
    public ResponseEntity<LikedMessageDto> createLikedMessage(@RequestBody LikedMessageDto likedMessageDto) {
        Message parentMessage = this.messageService.findByIdMessage(likedMessageDto.getIdMessage());
        User parentUser = this.userService.getUserByIdUser(likedMessageDto.getIdUser());
        LikedMessage likedMessage = modelMapper.map(likedMessageDto, LikedMessage.class);
        if (parentMessage != null && parentUser != null) {
            this.userService.saveMessageLike(likedMessage);
            return new ResponseEntity<>(likedMessageDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/user/messages/liked")
    public Set<LikedMessageDto> getAllUserLikedMessages(@RequestBody Long idUser) {
        User parentUser = this.userService.getUserByIdUser(idUser);
        return parentUser.getLikedMessageSet()
                .stream()
                .map( messageLike -> this.modelMapper
                        .map(messageLike, LikedMessageDto.class))
                .collect(Collectors.toSet());
    }

    @DeleteMapping(value = "/user/message/unlike")
    public ResponseEntity<LikedMessageDto> deleteLikeFromMessage(@RequestBody Long idLikedMessage){
        this.userService.deleteMessage(idLikedMessage); // TODO deletujes tu message!!!
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //--------------------------------------------------------------------------------------
    //                                  ANSWERED QUESTION
    //--------------------------------------------------------------------------------------
    @PostMapping(value = "/user/answered/add")
    public ResponseEntity createAnsweredQuestion(@RequestBody AnsweredQuestionDto answeredQuestionDto) {
        Question parentQuestion = this.questionService.findByIdQuestion(answeredQuestionDto.getIdQuestion());
        User parentUser = this.userService.getUserByIdUser(answeredQuestionDto.getIdUser());
        AnsweredQuestion answeredQuestion = modelMapper.map(answeredQuestionDto, AnsweredQuestion.class);
        if (parentQuestion != null && parentUser != null) {
            this.userService.saveAnsweredQuestion(answeredQuestion);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }


    @GetMapping(value = "/user/answered/all")
    public List<AnsweredQuestionDto> getAllQuestionAnswers(@RequestBody Long idUser) {
        User parentUser = this.userService.getUserByIdUser(idUser);
        return parentUser.getAnsweredQuestionSet()
                .stream()
                .map( answeredQuestion -> this.modelMapper
                        .map(answeredQuestion, AnsweredQuestionDto.class))
                .collect(Collectors.toList());
    }
}
