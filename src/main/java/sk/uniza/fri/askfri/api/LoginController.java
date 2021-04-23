package sk.uniza.fri.askfri.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.login.LoginForm;
import sk.uniza.fri.askfri.model.dto.login.LoginResponse;
import sk.uniza.fri.askfri.model.dto.login.UserDetailsDto;
import sk.uniza.fri.askfri.security.jwt.JwtService;
import sk.uniza.fri.askfri.service.IUserService;

/**
 * Controller - endpoint pre prihlasenie pouzivatela
 * pristup je volny pre POST metodu
 * cesta: /api
 *
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder encoder;

    private final IUserService userService;

    public LoginController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * Metoda pre prihlasenie pouzivatela do aplikacie pomocou POST requestu
     * Obsahuje autentifikaciu pouzivatela a vytvorenie jwt
     * cesta:  /api/login
     * @param loginForm Obsahuje email a heslo pouzivatela z vyplneneho prihlasovacieho formulara
     * @return ResponseEntity<LoginResponse> Pokial bol pouzivatel autentifikovany, vrati LoginResponse
     *                                       obsahujuci ID,JWT a Rolu pouzivatela
     *                                       Vracia null miesto ResponseDto ak zlyhala autentifikacia
     */
    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginForm loginForm) {

        Authentication authentication =  authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = this.jwtService.generateJwtToken(authentication);
        UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();
        try {
            User user =  this.userService.getUserByEmail(userDetails.getUsername());
            return new ResponseEntity<LoginResponse>(new LoginResponse(jwt, user.getIdUser(), user.getRole()), HttpStatus.OK);

        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
