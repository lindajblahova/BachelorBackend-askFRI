package sk.uniza.fri.askfri.api;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.model.dto.ResponseDto;
import sk.uniza.fri.askfri.model.dto.RoomDto;
import sk.uniza.fri.askfri.security.jwt.PreAuthServiceImplement;
import sk.uniza.fri.askfri.service.*;
import java.util.Set;

/**
 * Controller - endpoint pre pracu s miestnostami
 * pre pristup je podmienka platneho jwt
 * cesta: /api/rooms/*
 *
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/rooms")
@PreAuthorize("isAuthenticated()")
public class RoomController {

    private final IRoomService roomService;
    private final PreAuthServiceImplement preAuthServiceImplement;

    public RoomController(IRoomService roomService, PreAuthServiceImplement preAuthServiceImplement) {
        this.roomService = roomService;
        this.preAuthServiceImplement = preAuthServiceImplement;
    }

    /**
     * Metoda pre ziskanie udajov miestnosti na zaklade jej id pomocou GET requestu
     * cesta:  /api/rooms/get/room/{id}
     * @param idRoom ID miestnosti ktorej vlastnosti je potrebne ziskat (Long)
     * @return ResponseEntity<RoomDto> Vracia RoomDto obsahujuce udaje miestnosti
     *                                 Vracia null miesto RoomDto ak miestnost nebola najdena
     */
    @GetMapping("/get/room/{id}")
    @PreAuthorize("hasRole('Student') || hasRole('Vyucujuci')")
    public ResponseEntity<RoomDto> getRoom(@PathVariable("id") Long idRoom) {
        try {
            RoomDto room = this.roomService.findRoomDtoByIdRoom(idRoom);
            return new ResponseEntity<RoomDto>(room, HttpStatus.OK);
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }

    /**
     * Metoda pre ziskanie vsetkych miestnosti pouzivatela na zaklade jeho ID pomocou GET requestu
     * cesta:  /api/rooms/get/user/{id}
     * @param idUser ID pouzivatela ktoreho miestnosti je potrebne ziskat (Long)
     * @return ResponseEntity<Set<RoomDto>> Vracia Set miestnosti RoomDto ktore vlastni zadany pouzivatel
     *                                      Vracia null miesto Setu ak pouzivatel nebol najdeny
     */
    @GetMapping("/get/user/{id}")
    @PreAuthorize("@preAuthServiceImplement.isWhoHeSaysHeIs(#token,#idUser)==true")
    public ResponseEntity<Set<RoomDto>> getAllUserRooms(@PathVariable("id") Long idUser, @RequestHeader (name="Authorization") String token) {
        try {
            return new ResponseEntity<Set<RoomDto>>(this.roomService.findUserRooms(idUser), HttpStatus.OK);
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Metoda pre vytvorenie miestnosti pomocou POST requestu
     * pred pristupom k vytvoreniu miestnosti kontroluje, ci uz pristupovy kod nie je obsadeny
     * cesta:  /api/rooms/add
     * @param roomDto Obsahuje ID pouzivatela a udaje pre novu miestnost (RoomDto)
     * @return ResponseEntity<ResponseDto> Vracia ResponseDto so spravou o vytvoreni miestnosti
     *                                     Vracia null miesto ResponseDto ak pouzivatel nebol najdeny
     *                                     alebo niektore udaje boli zadane zle
     */
    @PostMapping(value = "/add")
    @PreAuthorize("@preAuthServiceImplement.isWhoHeSaysHeIs(#token,#roomDto.idOwner)==true")
    public ResponseEntity<ResponseDto> createRoom(@RequestBody RoomDto roomDto, @RequestHeader (name="Authorization") String token) {
        try {
            if (this.roomService.existsRoomByPasscodeAndActive(roomDto.getRoomPasscode())) {
                return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            }
            ResponseDto responseDto = this.roomService.createRoom(roomDto);
            return new ResponseEntity<ResponseDto>(responseDto, HttpStatus.OK);
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }

    /**
     * Metoda pre zistenie, ci je pristupovy kod aktualne pouzity pomocou GET requestu
     * Je pouzita pri vytvarani noveho kodu pre novu miestnost alebo pri update neaktivnej miestnosti
     * na aktivnu
     * cesta:  /api/rooms/get/passcode/{passcode}
     * @param passcode Obsahuje aktualne zadany kod miestnosti pri vytvoreni alebo aktualny
     *                 kod miestnosti pri zapinani aktivity (String)
     * @return boolean Vracia boolean podla toho, ci bola najdena nejaka aktivna miestnost s rovnakym
     *                 pristupovym kodom (=true), alebo nie (=false)
     */
    @GetMapping(value = "/get/passcode/{passcode}")
    @PreAuthorize("hasRole('Student') || hasRole('Vyucujuci')")
    public boolean isPasscodeCurrentlyUsed(@PathVariable("passcode") String passcode) {
       return this.roomService.existsRoomByPasscodeAndActive(passcode);
    }

    /**
     * Metoda pre zistenie, udajov aktivnej miestnosti z jej pristupoveho kodu pomocou GET requestu
     * Je pouzita pri pristupe do miestnosti ucastnikom, ktory ma k dispozii jej pristupovy kod
     * cesta:  /api/rooms/get/room-passcode/{passcode}
     * @param passcode Obsahuje zadany kod z formulara pre pristup do miestnosti (String)
     * @return ResponseEntity<RoomDto> Vracia RoomDto, podla toho, ci bola najdena aktivna miestnost
     *                                 s pozadovanym pristupovym kodom
     *                                 Pokial miestnost nebola najdena miesto RoomDto vrati null
     */
    @GetMapping(value = "/get/room-passcode/{passcode}")
    @PreAuthorize("hasRole('Student') || hasRole('Vyucujuci')")
    public ResponseEntity<RoomDto> getActiveRoomByPasscode(@PathVariable("passcode") String passcode) {
        try {
            RoomDto roomDto = this.roomService.findRoomByPasscodeAndActive(passcode);
            return new ResponseEntity<>(roomDto, HttpStatus.OK);
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Metoda pre upravu pristupoveho kodu miestnosti pomocou PUT requestu
     * Je pouzita pri odoslanej poziadavke na zmenu pristupoveho kodu, pri zapnuti aktivity miestnosti,
     * pokial aktualny pristupovy kod nie je momentalne dostupny
     * Po zmene pristupoveho kodu je upravena aj aktivita miestnosti
     * cesta:  /api/rooms/update/passcode
     * @param roomDto Obsahuje zadane udaje miestnosti s novym kodom miestnosti (RoomDto)
     * @return ResponseEntity<ResponseDto> Vracia ResponseDto, so spravou o zmene pristupoveho kodu
     *                                     Pokial miestnost nebola najdena, pristupovy kod je aktualne
     *                                     pouzivany alebo nebol zadany, miesto RoomDto vrati null
     */
    @PutMapping(value = "/update/passcode")
    @PreAuthorize("@preAuthServiceImplement.isRoomOwnerFromRoom(#token,#roomDto.idRoom)==true")
    public ResponseEntity<ResponseDto> updateRoomPasscode(@RequestBody RoomDto roomDto, @RequestHeader (name="Authorization") String token) {
        try {
            ResponseDto responseDto = this.roomService.updateRoomPasscode(roomDto);
            return new ResponseEntity<>(responseDto,HttpStatus.OK);
        } catch (IllegalArgumentException e ) {
            return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Metoda pre upravu aktivity miestnosti pomocou PUT requestu
     * Je pouzita pri odoslanej poziadavke na zmenu aktivity miestnosti
     * cesta:  /api/rooms/update/activity
     * @param idRoom ID miestnosti, ktorej aktivitu je pozadovane upraviť (Long)
     * @return ResponseEntity<ResponseDto> Vracia ResponseDto, so spravou o zmene aktivity miestnosti
     *                                     Pokial miestnost nebola najdena miesto RoomDto vrati null
     */
    @PutMapping(value = "/update/activity")
    @PreAuthorize("@preAuthServiceImplement.isRoomOwnerFromRoom(#token,#idRoom)==true")
    public ResponseEntity<ResponseDto> updateRoomActivity(@RequestBody Long idRoom, @RequestHeader (name="Authorization") String token) {
        try {
            ResponseDto responseDto = this.roomService.updateRoomActivity(idRoom);
            return new ResponseEntity<>(responseDto,HttpStatus.OK);
        } catch (NullPointerException e ) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Metoda pre vymazanie miestnosti pomocou DELETE requestu
     * Je pouzita pri odoslanej poziadavke na zmenu aktivity miestnosti
     * cesta:  /api/rooms/delete/{id}
     * @param idRoom ID miestnosti, ktoru je potrebne vymazat (Long)
     * @return ResponseEntity<ResponseDto> Vracia ResponseDto, so spravou o vymazani miestnosti
     *                                     Pokial miestnost nebola najdena miesto RoomDto vrati null
     */
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("@preAuthServiceImplement.isRoomOwnerFromRoom(#token,#idRoom)==true")
    public ResponseEntity<ResponseDto> deleteRoom(@PathVariable("id") Long idRoom, @RequestHeader (name="Authorization") String token) {
        try {
            ResponseDto responseDto = this.roomService.deleteRoom(idRoom);
            return new ResponseEntity<>(responseDto,HttpStatus.OK);
        } catch (EmptyResultDataAccessException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }
}
