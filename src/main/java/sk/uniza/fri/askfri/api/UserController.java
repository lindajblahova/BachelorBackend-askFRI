package sk.uniza.fri.askfri.api;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.UserDto;
import sk.uniza.fri.askfri.service.IUserService;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private IUserService userService;
    private ModelMapper modelMapper;

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

    @DeleteMapping(value = "/delete")
    public ResponseEntity deleteUser(@RequestBody long id) {
            this.userService.deleteUser(id);
            return new ResponseEntity(HttpStatus.OK);
    }

}
