package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.Question;
import sk.uniza.fri.askfri.model.dto.OptionalAnswerDto;
import sk.uniza.fri.askfri.model.dto.QuestionDto;
import sk.uniza.fri.askfri.model.dto.ResponseDto;

import java.util.Set;

/** Interface pre sluzbu pracujucu s Question
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
public interface IQuestionService {

    /** Metoda pre vytvorenie otazky
     * @param questionDto DTO otazka
     * @return QuestionDto vytvorena otazka
     * @throws NullPointerException pokial nebola najdena rodicovska miestnost alebo
     * obsah otazky bol prazdny
     */
    QuestionDto createQuestion(QuestionDto questionDto);

    /** Metoda pre ulozenie otazky
     * @param question otazka
     * @return Question ulozena otazka
     */
    Question saveQuestion(Question question);

    /** Metoda pre zmazanie otazky
     * @param idQuestion ID otazky
     * @return ResponseDto odpoved o zmazani otazky
     */
    ResponseDto deleteQuestion(Long idQuestion);

    /** Metoda pre najdenie otazky podla jej ID
     * @param idQuestion ID otazky
     * @return Question najdena otazka
     */
    Question findByIdQuestion(Long idQuestion);

    /** Metoda pre najdenie setu vsetkych otazok pre danu miestnost
     * @param idRoom ID miestnosti
     * @return Set<QuestionDto> set vsetkych otazok pre danu miestnost
     * @throws NullPointerException pokial nenajde zadanu miestnost
     */
    Set<QuestionDto> findQuestionsByRoom(Long idRoom);

    /** Metoda pre najdenie setu aktivnych otazok pre danu miestnost
     * @param idRoom ID miestnosti
     * @return Set<QuestionDto> set aktivnych otazok pre danu miestnost
     * @throws NullPointerException pokial nenajde zadanu miestnost
     */
    Set<QuestionDto> findQuestionsByRoomAndActive(Long idRoom);

    /** Metoda pre upravenie viditelnosti otazky pre ucastnikov
     * @param idQuestion ID otazky
     * @return ResponseDto odpoved o zmene viditelnosti otazky
     * @throws NullPointerException ak nebola najdena otazka alebo miestnost
     */
    ResponseDto updateQuestionDisplayed(Long idQuestion);

    /** Metoda pre upravenie viditelnosti vysledkov otazky pre ucastnikov
     * @param idQuestion ID otazky
     * @return ResponseDto odpoved o zmene viditelnosti vysledkov otazky
     * @throws NullPointerException ak nebola najdena otazka alebo miestnost
     */
    ResponseDto updateAnswersDisplayed(Long idQuestion);

    /** Metoda pre pridanie moznosti k otazke
     * @param optionalAnswerDto pole moznosti pre otazku
     * @return ResponseDto odpoved o ulozeni moznosti pre otazku
     * @throws NullPointerException ak nebola najdena otazka
     * @throws IllegalArgumentException ak bola niektora z moznosti prazdna
     */
    ResponseDto addOptionalAnswer(OptionalAnswerDto[] optionalAnswerDto);

    /** Metoda pre ziskanie moznosti k otazke
     * @param idQuestion ID otazky
     * @return Set<OptionalAnswerDto> Set DTO moznosti k pozadovanej otazke
     * @throws NullPointerException ak nebola najdena otazka
     */
    Set<OptionalAnswerDto> getQuestionOptionalAnswers(Long idQuestion);

    /** Metoda pre zistenie, ci je pouzivatel autorom miestnosti
     * @param idUser ID pouzivatela
     * @param idQuestion ID otazky, s ktorou chce pouzivatel vykonat akciu
     * @return Integer pocet najdenych riadkov pouzivatela
     */
    Integer isUserRoomOwnerFromQuestion(Long idUser, Long idQuestion);
}
