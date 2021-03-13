package sk.uniza.fri.askfri.api;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.UserDto;
import sk.uniza.fri.askfri.service.IRoomService;
import sk.uniza.fri.askfri.service.IUserService;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final IUserService userService;
    private final ModelMapper modelMapper;

    public UserController(IUserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/all")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map( user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/user")
    public UserDto getUser(@RequestBody String email) {
        boolean userExists = this.userService.existsByEmail(email);
        if (userExists) {
            User foundUser = this.userService.getUserByEmail(email);
            return modelMapper.map(foundUser, UserDto.class);
        }
        return null; // TODO exception
    }

    @PutMapping(value = "/update")
    public ResponseEntity updateUser(@RequestBody UserDto userDto) {
        User foundUser = this.userService.getUserByEmail(userDto.getEmail());
        if (foundUser.getIdUser() != null) {
            foundUser.setPassword(userDto.getPassword());
            this.userService.saveUser(foundUser);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity deleteUser(@RequestBody String email) {
        User roomsOwner = this.userService.getUserByEmail(email);
        this.userService.deleteUser(roomsOwner.getIdUser());
        return new ResponseEntity(HttpStatus.OK);
    }

}
