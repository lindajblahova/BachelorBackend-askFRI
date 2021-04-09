package sk.uniza.fri.askfri.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.dao.IUserRepository;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.LoginForm;
import sk.uniza.fri.askfri.model.dto.LoginResponse;
import sk.uniza.fri.askfri.model.dto.UserDetailsDto;
import sk.uniza.fri.askfri.security.jwt.JwtService;
import sk.uniza.fri.askfri.service.IUserService;

@CrossOrigin(origins = "*")
@RestController
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

    @Autowired
    private IUserRepository userRepository;

    private final IUserService userService;

    public LoginController(IUserService userService) {
        this.userService = userService;
    }


    @PostMapping(value = "/login", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginForm loginForm) {

        Authentication authentication =  authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtService.generateJwtToken(authentication);
        UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();
        User user =  userRepository.findByEmail(userDetails.getUsername());

        return new ResponseEntity<LoginResponse>(new LoginResponse(jwt, user.getIdUser(), user.getRole()), HttpStatus.OK);
    }
}
