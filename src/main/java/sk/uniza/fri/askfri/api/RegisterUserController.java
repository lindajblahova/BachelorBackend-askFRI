package sk.uniza.fri.askfri.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.model.dto.ResponseDto;
import sk.uniza.fri.askfri.model.dto.user.UserDto;
import sk.uniza.fri.askfri.service.IUserService;

import javax.persistence.EntityExistsException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class RegisterUserController {

    private final IUserService userService;

    public RegisterUserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDto> registerUser(@RequestBody UserDto user) {
        try {
            ResponseDto responseDto = this.userService.createUser(user);
            return new ResponseEntity<ResponseDto>(responseDto,HttpStatus.OK);
        } catch (EntityExistsException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }

    }

}
