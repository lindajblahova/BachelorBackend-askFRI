package sk.uniza.fri.askfri.api;

import nonapi.io.github.classgraph.json.JSONUtils;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
;
import sk.uniza.fri.askfri.model.*;
import sk.uniza.fri.askfri.model.dto.*;
import sk.uniza.fri.askfri.service.*;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")

public class UserController {

    private final IUserService userService;
    private final IMessageService messageService;
    private final IRoomService roomService;
    private final ModelMapper modelMapper;
    private final IQuestionService questionService;
    private final PasswordEncoder passwordEncoder;

    public UserController(IUserService userService, IMessageService messageService, IRoomService roomService, ModelMapper modelMapper, IQuestionService questionService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.messageService = messageService;
        this.roomService = roomService;
        this.modelMapper = modelMapper;
        this.questionService = questionService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Set<UserProfileDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers()
                .stream()
                .map( user -> modelMapper.map(user, UserProfileDto.class))
                .collect(Collectors.toSet()), HttpStatus.OK);
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<UserProfileDto> getUserById(@PathVariable("id") long id) {
        User user = this.userService.getUserByIdUser(id);
        if (user != null) {
            return new ResponseEntity<>(this.modelMapper.map(user, UserProfileDto.class),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<ResponseDto> updateUser(@RequestBody UserPasswordDto userPasswordDto) {
        User foundUser = this.userService.getUserByIdUser(userPasswordDto.getIdUser());
        if (foundUser != null) {
            if (this.passwordEncoder.matches(userPasswordDto.getOldPassword(),foundUser.getPassword())
                && !userPasswordDto.getNewPassword().equals(""))
            {
                foundUser.setPassword(this.passwordEncoder.encode(userPasswordDto.getNewPassword()));
                this.userService.saveUser(foundUser);
                return new ResponseEntity<ResponseDto>(
                        new ResponseDto(userPasswordDto.getIdUser(), "Heslo bolo zmenené"),HttpStatus.OK);
            }

            return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }


    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<ResponseDto> deleteUser(@PathVariable("id") long id) {
        try {
          //  this.userService.deleteUserAnsweredQuestions(id);
           // this.roomService.deleteUserRooms(id);
            this.userService.deleteUser(id);
            return new ResponseEntity<>(new ResponseDto(id, "Používateľ bol vymazaný"),HttpStatus.OK);
        } catch (EmptyResultDataAccessException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    //--------------------------------------------------------------------------------------
    //                                  ANSWERED QUESTION
    //--------------------------------------------------------------------------------------

    @PostMapping(value = "/user/answered/add")
    public ResponseEntity<ResponseDto> createAnsweredQuestion(@RequestBody AnsweredQuestionDto answeredQuestionDto) {
        Question parentQuestion = this.questionService.findByIdQuestion(answeredQuestionDto.getIdQuestion());
        User parentUser = this.userService.getUserByIdUser(answeredQuestionDto.getIdUser());

        if (parentQuestion != null && parentUser != null ) {
            AnsweredQuestion answeredQuestion = new AnsweredQuestion(parentUser,parentQuestion);
            answeredQuestion = this.userService.saveAnsweredQuestion(answeredQuestion);
            parentUser.addAnsweredQuestion(answeredQuestion);
            parentQuestion.addAnsweredQuestion(answeredQuestion);
            this.userService.saveUser(parentUser);
            this.questionService.saveQuestion(parentQuestion);
            parentQuestion.getAnsweredQuestionSet().add(answeredQuestion);
            parentUser.getAnsweredQuestionSet().add(answeredQuestion);
            return new ResponseEntity<>(new ResponseDto(answeredQuestion.hashCode(),"Odpoveď používateľa bola zaznamená"),HttpStatus.OK);
        }
       return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping(value = "/user/answered/all/{id}")
    public ResponseEntity<Set<AnsweredQuestionDto>> getAllQuestionAnswers(@PathVariable("id") long idUser) {

        User parentUser = this.userService.getUserByIdUser(idUser);
        Set<AnsweredQuestion> answerSet = parentUser.getAnsweredQuestionSet();
        Set<AnsQuestionClass> dtoSet = new HashSet<AnsQuestionClass>();
        if (answerSet != null) {
            answerSet.forEach(data -> dtoSet.add(new AnsQuestionClass(data.getIdUser().getIdUser(),data.getIdQuestion().getIdQuestion())));
            return new ResponseEntity<>(dtoSet.stream().map(data -> this.modelMapper.map(data,
                    AnsweredQuestionDto.class)).collect(Collectors.toSet()), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    //--------------------------------------------------------------------------------------
    //                                  MESSAGE LIKE
    //--------------------------------------------------------------------------------------
    @PostMapping(value = "/user/message/like")
    public ResponseEntity<ResponseDto> createLikedMessage(@RequestBody LikedMessageDto likedMessageDto) {
        Message parentMessage = this.messageService.findByIdMessage(likedMessageDto.getIdMessage());
        User parentUser = this.userService.getUserByIdUser(likedMessageDto.getIdUser());
        if (parentMessage != null && parentUser != null) {
            LikedMessage likedMessage = new LikedMessage(parentUser,parentMessage);
            likedMessage = this.userService.saveLikedMessage(likedMessage);
            parentUser.addLikedMessage(likedMessage);
            parentMessage.addLikedMessage(likedMessage);
            this.userService.saveUser(parentUser);
            this.messageService.saveMessage(parentMessage);
            return new ResponseEntity<>(new ResponseDto(likedMessage.hashCode(),"Like bol zaznamenaný"), HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/user/message/unlike/{idMessage}/{idUser}")
    public ResponseEntity<ResponseDto> deleteLikeFromMessage(@PathVariable("idMessage") Long idMessage,
                                                             @PathVariable("idUser") Long idUser){
       try
       {
           LikedMessageId likedMessageId = new LikedMessageId(idUser,idMessage);
           User user = this.userService.getUserByIdUser(idUser);
           Message message = this.messageService.findByIdMessage(idMessage);
           LikedMessage likedMessage = this.messageService.findLikedMessage(idUser,idMessage);

           user.removeLikedMessage(likedMessage);
           message.removeLikedMessage(likedMessage);
           this.userService.deleteLikedMessage(likedMessageId);
           this.userService.saveUser(user);
           this.messageService.saveMessage(message);
           return new ResponseEntity<>(new ResponseDto(likedMessageId.hashCode(), "Like bol zrušený"), HttpStatus.OK);

       } catch (EmptyResultDataAccessException e)
       {
           return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
       }

    }
}
