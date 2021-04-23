package sk.uniza.fri.askfri.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.security.jwt.JwtService;
import sk.uniza.fri.askfri.service.IMessageService;
import sk.uniza.fri.askfri.service.IQuestionService;
import sk.uniza.fri.askfri.service.IRoomService;

/** Sluzba overujuca, ci je uzivatel autorom miestnosti a teda ma pravo vykonavat
 *  niektore akcie
 *  Pouzita ako @PreAuthorize
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@Service
public class PreAuthServiceImplement {

    @Autowired
    private JwtService tokenService;

    private final IMessageService messageService;
    private final IQuestionService questionService;
    private final IRoomService roomService;

    public PreAuthServiceImplement(IMessageService messageService, IQuestionService questionService, IRoomService roomService) {
        this.messageService = messageService;
        this.questionService = questionService;
        this.roomService = roomService;
    }

    /** Z tokenu vytiahne ID usera a pokusi sa najst ci sprava, s ktorou chce pouzivatel
     *  vykonat akciu patri do miestnosti ktorej je autorom
     * @param token token odoslany v headeri requestu
     * @param idMessage ID spravy, s ktorou chce pouzivatel vykonat akciu
     * @return boolean ci bol najdeny zaznam:  count>0=true -> je autorom
     */
    public boolean isRoomOwnerFromMessage(String token, Long idMessage) {
        Long idUser = this.getIdFromToken(token);
        Integer count = this.messageService.isUserRoomOwnerFromMessage(idUser,idMessage);
        return count > 0;
    }

    /** Z tokenu vytiahne ID usera a pokusi sa najst ci otazka, s ktorou chce pouzivatel
     *  vykonat akciu patri do miestnosti ktorej je autorom
     * @param token token odoslany v headeri requestu
     * @param idQuestion ID otazky, s ktorou chce pouzivatel vykonat akciu
     * @return boolean ci bol najdeny zaznam:  count>0=true -> je autorom
     */
    public boolean isRoomOwnerFromQuestion(String token, Long idQuestion) {
        Long idUser = this.getIdFromToken(token);
        Integer count = this.questionService.isUserRoomOwnerFromQuestion(idUser,idQuestion);
        return count > 0;
    }

    /** Z tokenu vytiahne ID usera a pokusi sa zistit ci je pouzivatel autorom
     *  miestnosti, s ktorou chce pouzivatel vykonat akciu
     * @param token token odoslany v headeri requestu
     * @param idRoom ID miestnosti, s ktorou chce pouzivatel vykonat akciu
     * @return boolean ci bol najdeny zaznam:  count>0=true -> je autorom
     */
    public boolean isRoomOwnerFromRoom(String token, Long idRoom) {
        Long idUser = this.getIdFromToken(token);
        Integer count = this.roomService.isUserRoomOwner(idUser,idRoom);
        return count > 0;
    }

    /** Z tokenu vytiahne ID usera a pokusi sa zistit ci je pouzivatel pouzivatelom,
     *  s ktorym chce pouzivatel vykonat akciu
     * @param token token odoslany v headeri requestu
     * @param idUser ID pouzivatela, s ktorym chce pouzivatel vykonat akciu
     * @return boolean ci bol najdeny zaznam:  ak su rovnake -> je to on (opravnena akcia)
     */
    public boolean isWhoHeSaysHeIs(String token, Long idUser) {
        Long idUserFromToken = this.getIdFromToken(token);
        return idUserFromToken.equals(idUser);
    }

    /** Privatna metoda pre ziskanie ID usera z tokenu
     * @param token token odoslany v headeri requestu
     * @return Long ID pouzivatela
     */
    private Long getIdFromToken(String token) {
        String jwt = token.split(" ")[1];
        return Long.parseLong(this.tokenService.getIdFromToken(jwt));
    }
}
