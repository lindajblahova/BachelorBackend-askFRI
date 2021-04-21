package sk.uniza.fri.askfri.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.model.dto.RoomDto;
import sk.uniza.fri.askfri.model.dto.user.UserProfileDto;
import sk.uniza.fri.askfri.service.IRoomService;
import sk.uniza.fri.askfri.service.IUserService;

import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/admin")
public class AdminController {

    private final IUserService userService;
    private final IRoomService roomService;

    public AdminController(IUserService userService, IRoomService roomService) {
        this.userService = userService;
        this.roomService = roomService;
    }

    @GetMapping(value = "/users/all")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Set<UserProfileDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/rooms/all")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Set<RoomDto>> getAllRooms() {
        return new ResponseEntity<Set<RoomDto>>(this.roomService.findAllRooms(), HttpStatus.OK);
    }

}
