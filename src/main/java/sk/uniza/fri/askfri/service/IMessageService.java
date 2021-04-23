package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.Message;
import sk.uniza.fri.askfri.model.dto.MessageDto;
import sk.uniza.fri.askfri.model.dto.MessageWithLikes;
import sk.uniza.fri.askfri.model.dto.ResponseDto;

import java.util.Set;

/** Interface pre sluzbu pracujucu s Message
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
public interface IMessageService {

    /**
     * Metoda pre vytvorenie spravy
     *
     * @param messageDto DTO sprava
     * @return ResponseDto odpoved o ulozeni spravy
     * @throws NullPointerException pokial nebola najdena miestnost alebo bola prazdna
     *                              sprava
     */
    ResponseDto createMessage(MessageDto messageDto);

    /**
     * Metoda pre ulozenie spravy
     *
     * @param message sprava
     * @return Message ulozena sprava
     */
    Message saveMessage(Message message);

    /**
     * Metoda pre najdenie setu vsetkych sprav pre danu miestnost
     *
     * @param idRoom ID miestnosti
     * @return Set<MessageWithLikes> set odpovedi s reakciami pre danu miestnost
     * @throws NullPointerException pokial nie je najdena miestnost
     */
    Set<MessageWithLikes> getAllRoomMessages(Long idRoom);

    /**
     * Metoda pre zmazanie spravy
     *
     * @param idMessage ID spravy
     * @return ResponseDto odpoved o zmazani spravy
     * @throws NullPointerException ak nebola najdena miestnost alebo sprava
     */
    ResponseDto deleteMessage(Long idMessage);

    /**
     * Metoda pre najdenie spravy podla jej ID
     *
     * @param idMessage ID spravy
     * @return Message najdena sprava
     */
    Message findByIdMessage(Long idMessage);

    /** Metoda pre zistenie, ci je pouzivatel autorom miestnosti
     * @param idUser ID pouzivatela
     * @param idMessage ID spravy, s ktorou chce pouzivatel vykonat akciu
     * @return Integer pocet najdenych riadkov pouzivatela
     */
    Integer isUserRoomOwnerFromMessage(Long idUser, Long idMessage);
}
