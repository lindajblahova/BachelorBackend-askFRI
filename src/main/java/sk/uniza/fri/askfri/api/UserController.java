package sk.uniza.fri.askfri.api;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.*;
import sk.uniza.fri.askfri.model.dto.user.UserPasswordDto;
import sk.uniza.fri.askfri.model.dto.user.UserProfileDto;
import sk.uniza.fri.askfri.service.*;

import java.util.*;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
public class UserController {

    private final IUserService userService;
    private final IAnsweredQuestionService answeredQuestionService;
    private final ILikedMessageService likedMessageService;
    private final IRoomService roomService;
    private final IAnswerService answerService;
    private final IQuestionService questionService;
    private final IMessageService messageService;

    public UserController(IUserService userService, IAnsweredQuestionService answeredQuestionService, ILikedMessageService likedMessageService, IRoomService roomService, IAnswerService answerService, IQuestionService questionService, IMessageService messageService) {
        this.userService = userService;
        this.answeredQuestionService = answeredQuestionService;
        this.likedMessageService = likedMessageService;
        this.roomService = roomService;
        this.answerService = answerService;
        this.questionService = questionService;
        this.messageService = messageService;
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<UserProfileDto> getUserById(@PathVariable("id") long id) {
        try {
            UserProfileDto user = this.userService.getUserProfileByIdUser(id);
            return new ResponseEntity<>(user,HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping(value = "/update")
    public ResponseEntity<ResponseDto> updateUser(@RequestBody UserPasswordDto userPasswordDto) {
        try {
            ResponseDto responseDto = this.userService.updateUserPassword(userPasswordDto);
            return new ResponseEntity<ResponseDto>(responseDto,HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
        }
    }


    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<ResponseDto> deleteUser(@PathVariable("id") Long id) {
        try {
            User user = this.userService.findUserByIdUser(id);
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
        try {
            ResponseDto responseDto = this.answeredQuestionService.saveAnsweredQuestion(answeredQuestionDto);
            return new ResponseEntity<>(responseDto,HttpStatus.OK);
        } catch (IllegalArgumentException e)
        {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping(value = "/user/answered/all/{id}")
    public ResponseEntity<Set<AnsweredQuestionDto>> getAllQuestionAnswers(@PathVariable("id") Long idUser) {

        try {
            Set<AnsweredQuestionDto> answerSet = this.answeredQuestionService.getAllUserQuestionAnswers(idUser);
            return new ResponseEntity<>(answerSet, HttpStatus.OK);
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    //--------------------------------------------------------------------------------------
    //                                  MESSAGE LIKE
    //--------------------------------------------------------------------------------------
    @PostMapping(value = "/user/message/like")
    public ResponseEntity<ResponseDto> createLikedMessage(@RequestBody LikedMessageDto likedMessageDto) {
        try {
            ResponseDto responseDto = this.likedMessageService.saveLikedMessage(likedMessageDto);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/user/message/unlike/{idMessage}/{idUser}")
    public ResponseEntity<ResponseDto> deleteLikeFromMessage(@PathVariable("idMessage") Long idMessage,
                                                             @PathVariable("idUser") Long idUser){
            ResponseDto responseDto = this.likedMessageService.deleteLikedMessage(idMessage, idUser);
            if (responseDto != null)
            {
                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }
}
