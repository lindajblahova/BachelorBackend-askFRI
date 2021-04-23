package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.dto.LikedMessageDto;
import sk.uniza.fri.askfri.model.dto.ResponseDto;

/** Interface pre sluzbu pracujucu s LikedMessage
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
public interface ILikedMessageService {

    /** Metoda pre ulozenie reakcie na spravu pouzivatelom
     * @param likedMessageDto DTO reakcia pouzivatela na spravu
     * @return ResponseDto odpoved o ulozeni reakcie na spravu
     * @throws NullPointerException ak nebola najdena sprava alebo pouzivatel
     */
    ResponseDto saveLikedMessage(LikedMessageDto likedMessageDto);

    /** Metoda pre zmazanie reakcie na spravu pouzivatelom
     * @param idMessage ID spravy na ktoru pouzivatel reagoval
     * @param idUser ID pouzivatela, ktory na spravu reagoval
     * @return ResponseDto odpoved o zmazani reakcie na spravu pouzivatelom
     */
    ResponseDto deleteLikedMessage(Long idMessage,Long idUser);

}
