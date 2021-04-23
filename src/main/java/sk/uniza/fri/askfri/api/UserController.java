package sk.uniza.fri.askfri.api;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
;
import sk.uniza.fri.askfri.model.dto.*;
import sk.uniza.fri.askfri.model.dto.user.UserPasswordDto;
import sk.uniza.fri.askfri.model.dto.user.UserProfileDto;
import sk.uniza.fri.askfri.service.*;
import sk.uniza.fri.askfri.security.jwt.PreAuthServiceImplement;

import java.util.*;

/**
 * Controller - endpoint pre pracu s miestnostami
 * pre pristup je podmienka platneho jwt
 * cesta: /api/users/*
 *
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
@PreAuthorize("isAuthenticated()")
public class UserController {

    private final IUserService userService;
    private final IAnsweredQuestionService answeredQuestionService;
    private final ILikedMessageService likedMessageService;
    private final PreAuthServiceImplement preAuthServiceImplement;

    public UserController(IUserService userService, IAnsweredQuestionService answeredQuestionService,
                          ILikedMessageService likedMessageService, PreAuthServiceImplement preAuthServiceImplement) {
        this.userService = userService;
        this.answeredQuestionService = answeredQuestionService;
        this.likedMessageService = likedMessageService;
        this.preAuthServiceImplement = preAuthServiceImplement;
    }

    /**
     * Metoda pre ziskanie profilovych udajov pouzivatela na zaklade jeho id pomocou GET requestu
     * cesta:  /api/users/user/{id}
     * @param idUser ID pouzivatela, ktoreho vlastnosti je potrebne ziskat (Long)
     * @return ResponseEntity<UserProfileDto> Vracia UserProfileDto obsahujuce udaje pouzivatela - id,
     *                                        meno, priezvisko a email
     *                                        Vracia null miesto UserProfileDto ak pouzivatel nebol
     *                                        najdeny
     */
    @GetMapping(value = "/user/{id}")
    @PreAuthorize("@preAuthServiceImplement.isWhoHeSaysHeIs(#token,#idUser)==true")
    public ResponseEntity<UserProfileDto> getUserById(@PathVariable("id") Long idUser, @RequestHeader (name="Authorization") String token) {
        try {
            UserProfileDto user = this.userService.getUserProfileByIdUser(idUser);
            return new ResponseEntity<>(user,HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }

    }

    /**
     * Metoda pre upravenie hesla pouzivatela pomocou PUT requestu
     * cesta:  /api/users/update
     * @param userPasswordDto Obsahuje udaje ako ID pouzivatela, zadane stare a nove heslo (UserPasswordDto)
     * @return ResponseEntity<ResponseDto> Vracia ResponseDto obsahujuce spravu o zmene hesla
     *                                     Vracia null miesto ResponseDto ak pouzivatel nebol
     *                                     najdeny, aktualne heslo nebolo spravne alebo nove heslo
     *                                     bolo prazdne
     *
     */
    @PutMapping(value = "/update")
    @PreAuthorize("@preAuthServiceImplement.isWhoHeSaysHeIs(#token,#userPasswordDto.idUser)==true")
    public ResponseEntity<ResponseDto> updateUser(@RequestBody UserPasswordDto userPasswordDto,
                                                  @RequestHeader (name="Authorization") String token) {
        try {
            String pattern = "(^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$)";
            if (!userPasswordDto.getNewPassword().matches(pattern))
            {
                return new ResponseEntity<>(null,HttpStatus.FORBIDDEN);
            }
            ResponseDto responseDto = this.userService.updateUserPassword(userPasswordDto);
            return new ResponseEntity<ResponseDto>(responseDto,HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Metoda pre vymazanie pouzivatela pomocou DELETE requestu
     * cesta:  /api/users/delete/{id}
     * @param idUser ID pouzivatela, ktoreho je potrebne vymazat (Long)
     * @return ResponseEntity<ResponseDto> Vracia ResponseDto obsahujuce spravu o vymazani pouzivatela
     *                                     Vracia null miesto ResponseDto ak pouzivatel nebol
     *                                     najdeny
     */
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasRole('Admin') || @preAuthServiceImplement.isWhoHeSaysHeIs(#token,#idUser)==true")
    public ResponseEntity<ResponseDto> deleteUser(@PathVariable("id") Long idUser,
                                                  @RequestHeader (name="Authorization") String token) {
        try {
            ResponseDto dto = this.userService.deleteUser(idUser);
            return new ResponseEntity<>(dto,HttpStatus.OK);
        } catch (EmptyResultDataAccessException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    //--------------------------------------------------------------------------------------
    //                                  ANSWERED QUESTION
    //--------------------------------------------------------------------------------------

    /**
     * Metoda pre ziskanie vsetkych pouzivatelom zodpovedanych odpovedi v miestnosti pomocou
     * GET requestu
     * cesta:  /api/users/user/answered/all/{idUser}/{idRoom}
     * @param idUser ID pouzivatela, ktoreho set zodpovedanych otazok je potrebne zistit (Long)
     * @return ResponseEntity<Set<AnsweredQuestionDto>> Vracia Set zaznamov obsahujucich pouzivatela
     *                                                  a nim zodpovedane Otazky
     *                                                  Vracia null miesto Setu ak pouzivatel nebol
     *                                                  najdeny
     */
    @GetMapping(value = "/user/answered/all/{idUser}/{idRoom}")
    @PreAuthorize("@preAuthServiceImplement.isWhoHeSaysHeIs(#token,#idUser)==true")
    public ResponseEntity<Set<AnsweredQuestionDto>> getAllUserRoomAnsweredQuestions(@PathVariable("idUser") Long idUser,
                                                                          @RequestHeader (name="Authorization") String token,
                                                                          @PathVariable("idRoom") Long idRoom) {
        try {
            Set<AnsweredQuestionDto> answerSet = this.answeredQuestionService.getAllUserQuestionAnswers(idUser, idRoom);
            return new ResponseEntity<>(answerSet, HttpStatus.OK);
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    //--------------------------------------------------------------------------------------
    //                                  MESSAGE LIKE
    //--------------------------------------------------------------------------------------

    /**
     * Metoda pre pridanie zaznamu reakcie na spravu pouzivatelom pomocou POST requestu
     * cesta:  /api/users/user/message/like
     * @param likedMessageDto Obsahuje ID pouzivatela a ID spravy, na ktoru pouzivatel reagoval (LikedMessageDto)
     * @return ResponseEntity<ResponseDto> Vracia ResponseDto obsahujuce spravu o zaznamenani reakcie
     *                                     Vracia null miesto ResponseDto ak pouzivatel alebo sprava
     *                                     nebolq najdena
     */
    @PostMapping(value = "/user/message/like")
    @PreAuthorize("@preAuthServiceImplement.isWhoHeSaysHeIs(#token,#likedMessageDto.idUser)==true")
    public ResponseEntity<ResponseDto> createLikedMessage(@RequestBody LikedMessageDto likedMessageDto,
                                                          @RequestHeader (name="Authorization") String token) {
        try {
            ResponseDto responseDto = this.likedMessageService.saveLikedMessage(likedMessageDto);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Metoda pre odobranie zaznamu reakcie na spravu pouzivatelom pomocou DELETE requestu
     * cesta:  /api/users/user/message/unlike/{idMessage}/{idUser}
     * @param idMessage Obsahuje ID pouzivatela ktoreho reakciu je potrebne vymazat (Long)
     * @param idUser Obsahuje ID spravy, ktorej reakciu je potrebne vymazat (Long)
     * @return ResponseEntity<ResponseDto> Vracia ResponseDto obsahujuce spravu o vymazani reakcie
     *                                     Vracia null miesto ResponseDto ak pouzivatel alebo sprava
     *                                     nebolq najdena
     */
    @DeleteMapping(value = "/user/message/unlike/{idMessage}/{idUser}")
    @PreAuthorize("@preAuthServiceImplement.isWhoHeSaysHeIs(#token,#idUser)==true")
    public ResponseEntity<ResponseDto> deleteLikeFromMessage(@PathVariable("idMessage") Long idMessage,
                                                             @PathVariable("idUser") Long idUser,
                                                             @RequestHeader (name="Authorization") String token){
            ResponseDto responseDto = this.likedMessageService.deleteLikedMessage(idMessage, idUser);
            if (responseDto != null)
            {
                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }
}
