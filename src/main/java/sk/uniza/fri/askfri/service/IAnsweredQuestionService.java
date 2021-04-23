package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.dto.AnsweredQuestionDto;
import sk.uniza.fri.askfri.model.dto.ResponseDto;

import java.util.Set;

/** Interface pre sluzbu pracujucu s AnsweredQuestion
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
public interface IAnsweredQuestionService {

    /** Metoda pre ulozenie zodpovedanej otazky
     * @param answeredQuestionDto pouzivatelom zodpovedana otazka DTO
     * @return ResponseDto odpoved o ulozeni zaznamu zodpovedanej otazky
     * @throws NullPointerException pokial nebol najdeny pouzivatel alebo otazka
     */
    ResponseDto saveAnsweredQuestion(AnsweredQuestionDto answeredQuestionDto);

    /** Metoda pre najdenie vsetkych pouzivatelom zodpovedanych otazok pre
     *  danu miestnost
     * @param idUser ID pouzivatela
     * @param idRoom ID miestnosti
     * @return Set<AnsweredQuestionDto> set pouzivatelom zodpovedanych otazok
     * @throws NullPointerException pokial nebol najdeny pouzivatel
     */
    Set<AnsweredQuestionDto> getAllUserQuestionAnswers(Long idUser, Long idRoom);

}
