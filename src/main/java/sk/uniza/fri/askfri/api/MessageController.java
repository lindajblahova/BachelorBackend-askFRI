package sk.uniza.fri.askfri.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.model.dto.*;
import sk.uniza.fri.askfri.security.jwt.PreAuthServiceImplement;
import sk.uniza.fri.askfri.service.IMessageService;

import java.util.Set;

/**
 * Controller - endpoint pre pracu so spravami
 * pre pristup je podmienka platneho jwt
 * cesta: /api/messages/*
 *
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/messages")
@PreAuthorize("isAuthenticated()")
public class MessageController {

    private final IMessageService messageService;
    private final PreAuthServiceImplement preAuthServiceImplement;

    public MessageController(IMessageService messageService, PreAuthServiceImplement preAuthServiceImplement) {
        this.messageService = messageService;
        this.preAuthServiceImplement = preAuthServiceImplement;
    }

    /**
     * Metoda pre vytvorenie spravy pomocou POST requestu
     * cesta:  /api/messages/add
     * @param messageDto Sprava obsahujuca ID miestnosti do ktorej patri a svoje vlastnosti (MessageDto)
     * @return ResponseEntity<ResponseDto> Pokial bola sprava ulozena, vrati ResponseDto
     *                                     so spravou o ulozeni
     *                                     Vracia null miesto ResponseDto ak miestnost nebola najdena
     *                                     alebo obsah spravy bol prazdny
     */
    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('Student') || hasRole('Vyucujuci')")
    public ResponseEntity<ResponseDto> createMessage(@RequestBody MessageDto messageDto) {
        try {
            ResponseDto dto = this.messageService.createMessage(messageDto);
            return new ResponseEntity<ResponseDto>(dto, HttpStatus.OK);
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Metoda pre ziskanie vsetkych sprav, ktore patria k miestnosti pomocou POST requestu
     * cesta:  /api/messages/room/{id}
     * @param idRoom ID miestnosti ktorej zoznam sprav je pozadovany (Long)
     * @return ResponseEntity<Set<MessageWithLikes>> Vracia Set sprav miestnosti
     *                                               Vracia null miesto Setu ak miestnost
     *                                               nebola najdena
     */
    @GetMapping(value = "/room/{id}")
    @PreAuthorize("hasRole('Student') || hasRole('Vyucujuci')")
    public ResponseEntity<Set<MessageWithLikes>> getAllRoomMessages(@PathVariable("id") Long idRoom) {
        try {
            return new ResponseEntity<Set<MessageWithLikes>>(this.messageService.getAllRoomMessages(idRoom), HttpStatus.OK);
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Metoda pre vymazanie spravy pomocou DELETE requestu
     * cesta:  /api/messages/delete/{id}
     * @param idMessage ID spravy, ktoru je potrebne vymazat (Long)
     * @return ResponseEntity<ResponseDto> Pokial bola sprava zmazana, vrati ResponseDto
     *                                     so spravou o zmazani
     *                                     Vracia null miesto ResponseDto ak sprava nebola najdena
     */
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("@preAuthServiceImplement.isRoomOwnerFromMessage(#token,#idMessage)==true")
    public ResponseEntity<ResponseDto> deleteMessage(@PathVariable("id") Long idMessage, @RequestHeader (name="Authorization") String token) {
        try {
            ResponseDto dto = this.messageService.deleteMessage(idMessage);
            return new ResponseEntity<ResponseDto>(dto,HttpStatus.OK);
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

}
