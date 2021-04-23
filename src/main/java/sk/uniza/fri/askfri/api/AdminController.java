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

/**
 * Controller - endpoint pre pristup k administracii
 * pre pristup je podmienka mat rolu "Admin"
 * cesta: /api/admin/*
 *
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/admin")
@PreAuthorize("isAuthenticated()")
public class AdminController {

    private final IUserService userService;
    private final IRoomService roomService;

    /**
     * Parametricky konstruktor
     */
    public AdminController(IUserService userService, IRoomService roomService) {
        this.userService = userService;
        this.roomService = roomService;
    }

    /**
     * Metoda pre pristup k zoznamu profilov vsetkych uzivatelov pomocou GET requestu
     * cesta:  /api/admin/users/all
     * @return ResponseEntity<Set<UserProfileDto>> Vracia set vsetkych pouzivatelov
     */
    @GetMapping(value = "/users/all")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Set<UserProfileDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    /**
     * Metoda pre pristup k zoznamu vsetkych miestnosti pomocou GET requestu
     * cesta:  /api/admin/rooms/all
     * @return ResponseEntity<Set<RoomDto>> Vracia set vsetkych miestnosti
     */
    @GetMapping("/rooms/all")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Set<RoomDto>> getAllRooms() {
        return new ResponseEntity<Set<RoomDto>>(this.roomService.findAllRooms(), HttpStatus.OK);
    }
}
