package sk.uniza.fri.askfri.api;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.LoginResponse;
import sk.uniza.fri.askfri.model.dto.ResponseDto;
import sk.uniza.fri.askfri.model.dto.UserDto;
import sk.uniza.fri.askfri.service.IUserService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class RegisterUserController {

    private final IUserService userService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserController(IUserService userService, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/register", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDto> registerUser(@RequestBody UserDto user) {
        User newUser = modelMapper.map(user, User.class);
        if (!user.getEmail().equals("") && !user.getFirstname().equals("") &&
                !user.getSurname().equals("") && !user.getPassword().equals("") ) {
            if (userService.existsByEmail(user.getEmail())) {
                return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
            }
            newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
            newUser = this.userService.saveUser(newUser);
            return new ResponseEntity<ResponseDto>(new ResponseDto(newUser.getIdUser(),
                    "Pokračujte prihlásením"),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

}
