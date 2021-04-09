package sk.uniza.fri.askfri.api;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
;
import sk.uniza.fri.askfri.model.*;
import sk.uniza.fri.askfri.model.dto.AnsweredQuestionDto;
import sk.uniza.fri.askfri.model.dto.UserDto;
import sk.uniza.fri.askfri.model.dto.UserPasswordDto;
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
    private final PasswordEncoder passwordEncoder;

    public UserController(IUserService userService, IMessageService messageService, ModelMapper modelMapper, IQuestionService questionService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.messageService = messageService;
        this.modelMapper = modelMapper;
        this.questionService = questionService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = "/all")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map( user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/useremail/{email}")
    public ResponseEntity<User> getUser(@PathVariable("email") String email) {
        boolean userExists = this.userService.existsByEmail(email);
        if (userExists) {
            return new ResponseEntity<>(this.userService.getUserByEmail(email),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE); // TODO exception
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        User user = this.userService.getUserByIdUser(id);
        if (user != null) {
            return new ResponseEntity<>(user,HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST); // TODO exception
    }

    @PutMapping(value = "/update")
    public ResponseEntity<UserPasswordDto> updateUser(@RequestBody UserPasswordDto userPasswordDto) {
        User foundUser = this.userService.getUserByIdUser(userPasswordDto.getIdUser());
        if (foundUser != null) {
            if (this.passwordEncoder.matches(userPasswordDto.getOldPassword(),foundUser.getPassword()))
            {
                foundUser.setPassword(this.passwordEncoder.encode(userPasswordDto.getNewPassword()));
                this.userService.saveUser(foundUser);
                return new ResponseEntity<>(userPasswordDto,HttpStatus.OK);
            }

            return new ResponseEntity<>(null,HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") long id) {
        this.userService.deleteUser(id);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    //--------------------------------------------------------------------------------------
    //                                  ANSWERED QUESTION
    //--------------------------------------------------------------------------------------

    @PostMapping(value = "/user/answered/add")
    public ResponseEntity<AnsweredQuestionDto> createAnsweredQuestion(@RequestBody AnsweredQuestionDto answeredQuestionDto) {
        Question parentQuestion = this.questionService.findByIdQuestion(answeredQuestionDto.getIdQuestion());
        User parentUser = this.userService.getUserByIdUser(answeredQuestionDto.getIdUser());
        AnsweredQuestion answeredQuestion = modelMapper.map(answeredQuestionDto, AnsweredQuestion.class);
        if (parentQuestion != null && parentUser != null) {
            answeredQuestion = this.userService.saveAnsweredQuestion(answeredQuestion);
            AnsweredQuestionDto dto = this.modelMapper.map(answeredQuestion, AnsweredQuestionDto.class);
            return new ResponseEntity<>(dto,HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }


    @GetMapping(value = "/user/answered/all/{id}")
    public Set<AnsweredQuestion> getAllQuestionAnswers(@PathVariable("id") long idUser) {
        User parentUser = this.userService.getUserByIdUser(idUser);
        if (parentUser != null) {
            return  parentUser.getAnsweredQuestionSet();
        }
        return null;
    }

}
