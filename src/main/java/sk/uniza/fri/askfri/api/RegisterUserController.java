package sk.uniza.fri.askfri.api;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.UserDto;
import sk.uniza.fri.askfri.service.IUserService;

@RestController
@RequestMapping("/api")
public class RegisterUserController {

    private IUserService userService;
    private ModelMapper modelMapper;

    public RegisterUserController(IUserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/register", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity registerUser(@RequestBody UserDto user) {
        User newUser = modelMapper.map(user, User.class);
        if (user.getEmail() != null) {
            if (userService.existsByEmail(user.getEmail())) {
                return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
            }

            userService.saveUser(newUser);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
