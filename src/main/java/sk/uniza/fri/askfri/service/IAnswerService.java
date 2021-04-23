package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.dto.AnswerDto;
import sk.uniza.fri.askfri.model.dto.ResponseDto;

import java.util.Set;

/** Interface pre sluzbu pracujucu s Answer
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
public interface IAnswerService {

    /** Metoda pre vytvorenie odpovede k otazke
     * @param answerDto pole DTO odpovedi
     * @return ResponseDto odpoved o ulozeni zaznamu odpovede
     * @throws NullPointerException pokial nebola najdena otazka
     * @throws IllegalArgumentException pokial bola zadana prazdna odpoved
     */
    ResponseDto createAnswer(AnswerDto[] answerDto);

    /** Metoda pre najdenie setu vsetkych odpovedi pre danu otazku
     * @param idQuestion ID otazky
     * @return Set<AnswerDto> set odpovedi pre danu otazku
     * @throws NullPointerException ak otazka nie je najdena
     */
    Set<AnswerDto> getAllQuestionAnswers(Long  idQuestion);

}
