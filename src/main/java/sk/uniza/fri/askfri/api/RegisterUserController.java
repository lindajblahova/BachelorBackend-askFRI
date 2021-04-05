package sk.uniza.fri.askfri.api;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.UserDto;
import sk.uniza.fri.askfri.service.IUserService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class RegisterUserController {

    private final IUserService userService;
    private final ModelMapper modelMapper;

    public RegisterUserController(IUserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/register", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> registerUser(@RequestBody UserDto user) {
        User newUser = modelMapper.map(user, User.class);
        if (user.getEmail() != null && user.getFirstname() != null &&
                user.getSurname() != null && user.getPassword() != null) {
            if (userService.existsByEmail(user.getEmail())) {
                return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
            }

            return new ResponseEntity<>(userService.saveUser(newUser),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }
}
