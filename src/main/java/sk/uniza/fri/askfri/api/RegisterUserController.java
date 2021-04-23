package sk.uniza.fri.askfri.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.model.dto.ResponseDto;
import sk.uniza.fri.askfri.model.dto.user.UserDto;
import sk.uniza.fri.askfri.service.IUserService;

import javax.persistence.EntityExistsException;

/**
 * Controller - endpoint pre registraciu pouzivatela
 * pristup je volny
 * cesta: /api
 *
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class RegisterUserController {

    private final IUserService userService;

    public RegisterUserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * Metoda pre registraciu noveho pouzivatela do aplikacie pomocou POST requestu
     * cesta:  /api/register
     * @param userDto Obsahuje udaje potrebne pre registraciu
     * @return ResponseEntity<ResponseDto> Pokial bol pouzivatel uspesne zaregistrovany,
     *                                     vrati ResponseDto so spravou pre prihlasenie
     *                                     Vracia null miesto ResponseDto ak pouzivatel uz existuje
     *                                     alebo niektory zo vstupov bol zly
     */
    @PostMapping(value = "/register")
    public ResponseEntity<ResponseDto> registerUser(@RequestBody UserDto userDto) {
        try {
            String pattern = "(^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$)";
            String pattern2 = "(^[\\w]{1,}[\\w.+-]{0,}@[\\w-]{2,}([.][a-zA-Z]{2,}|[.][\\w-]{2,}[.][a-zA-Z]{2,})$)";
            if (!userDto.getPassword().matches(pattern) || !userDto.getEmail().matches(pattern2))
            {
                return new ResponseEntity<>(null,HttpStatus.FORBIDDEN);
            }
            ResponseDto responseDto = this.userService.createUser(userDto);
            return new ResponseEntity<ResponseDto>(responseDto,HttpStatus.OK);
        } catch (EntityExistsException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }

    }

}
