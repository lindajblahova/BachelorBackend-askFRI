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
