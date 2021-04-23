package sk.uniza.fri.askfri.api;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.model.dto.*;
import sk.uniza.fri.askfri.security.jwt.PreAuthServiceImplement;
import sk.uniza.fri.askfri.service.IQuestionService;

import java.util.Set;

/**
 * Controller - endpoint pre pracu s otazkami a ich moznostami
 * pre pristup je podmienka platneho jwt
 * cesta: /api/questions/*
 *
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/questions")
@PreAuthorize("isAuthenticated()")
public class QuestionController {

    private final IQuestionService questionService;
    private final PreAuthServiceImplement preAuthServiceImplement;

    public QuestionController(IQuestionService questionService, PreAuthServiceImplement preAuthServiceImplement) {
        this.questionService = questionService;
        this.preAuthServiceImplement = preAuthServiceImplement;
    }

    /**
     * Metoda pre vytvorenie novej otazky pomocou POST requestu
     * cesta:  /api/questions/add
     * @param questionDto Otazka obsahujuca ID miestnosti do ktorej patri a svoje vlastnosti (QuestionDto)
     * @return ResponseEntity<QuestionDto> Pokial bola sprava ulozena, vrati QuestionDto, ktora svoje
     *                                     udaje, potrebne pre mozne dalsie vytvorenie moznosti k otazke
     *                                     Vracia null miesto QuestionDto ak miestnost nebola najdena,
     *                                     obsah otazky bol prazny alebo typ otazky neexistuje
     */
    @PostMapping(value = "/add")
    @PreAuthorize("@preAuthServiceImplement.isRoomOwnerFromRoom(#token,#questionDto.idRoom)==true")
    public ResponseEntity<QuestionDto> createQuestion(@RequestBody QuestionDto questionDto, @RequestHeader (name="Authorization") String token) {
        try {
            QuestionDto dto = this.questionService.createQuestion(questionDto);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Metoda pre ziskanie vsetkych otazok miestnosti pomocou GET requestu
     * Metoda je pouzita autorom pre pristup k vsetkym otazkam miestnosti
     * cesta:  /api/questions/room/{id}
     * @param idRoom ID miestnosti ktorej otazky su pozadovane (Long)
     * @return ResponseEntity<Set<QuestionDto>> Vracia set vsetkych otazok patriacich k danej miestnosti
     *                                          Vracia null miesto setu, ak miestnost nebola najdena
     */
    @GetMapping(value = "/room/{id}")
    @PreAuthorize("@preAuthServiceImplement.isRoomOwnerFromRoom(#token,#idRoom)==true")
    public ResponseEntity<Set<QuestionDto>> getAllRoomQuestions(@PathVariable("id") Long idRoom, @RequestHeader (name="Authorization") String token) {
        try {
            return new ResponseEntity<Set<QuestionDto>>(this.questionService.findQuestionsByRoom(idRoom), HttpStatus.OK );
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Metoda pre ziskanie vsetkych AKTIVNYCH otazok miestnosti pomocou GET requestu
     * Metoda je pouzita ucastnikom pre pristup LEN k AKTIVNYM otazkam miestnosti
     * cesta:  /api/questions/room/active/{id}
     * @param idRoom ID miestnosti ktorej otazky su pozadovane (Long)
     * @return ResponseEntity<Set<QuestionDto>> Vracia set vsetkych AKTIVNYCH otazok patriacich k danej
     *                                          miestnosti
     *                                          Vracia null miesto setu, ak miestnost nebola najdena
     */
    @GetMapping(value = "/room/active/{id}")
    @PreAuthorize("hasRole('Student') || hasRole('Vyucujuci')")
    public ResponseEntity<Set<QuestionDto>> getRoomActiveQuestions(@PathVariable("id") Long idRoom) {
        try {
            return new ResponseEntity<Set<QuestionDto>>(this.questionService.findQuestionsByRoomAndActive(idRoom), HttpStatus.OK );
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Metoda pre upravu viditelnosti otazky pre ucastnikov pomocou PUT requestu
     * Metoda je pouzita autorom pre zobrazenie/skrytie otazky ucastnikom
     * cesta:  /api/questions/update/question
     * @param idQuestion ID otazky ktorej viditelnost ma byt upravena (Long)
     * @return ResponseEntity<ResponseDto> Vracia ResponseDto so spravou o aktualnej viditelnosti otazky
     *                                     Vracia null ak otazka nebola najdena
     */
    @PutMapping(value = "/update/question")
    @PreAuthorize("@preAuthServiceImplement.isRoomOwnerFromQuestion(#token,#idQuestion)==true")
    public ResponseEntity<ResponseDto> updateQuestionDisplayed(@RequestBody Long idQuestion, @RequestHeader (name="Authorization") String token) {
        try {
            ResponseDto dto = this.questionService.updateQuestionDisplayed(idQuestion);
            return new ResponseEntity<ResponseDto>(dto,HttpStatus.OK);
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Metoda pre upravu viditelnosti vysledkov patriacich k otazke pre ucastnikov pomocou PUT requestu
     * Metoda je pouzita autorom pre zobrazenie/skrytie vysledkov otazky ucastnikom
     * cesta:  /api/questions/update/answers
     * @param idQuestion ID otazky ktorej viditelnost vysledkov ma byt upravena (Long)
     * @return ResponseEntity<ResponseDto> Vracia ResponseDto so spravou o aktualnej viditelnosti vysledkov
     *                                     Vracia null ak otazka nebola najdena
     */
    @PutMapping(value = "/update/answers")
    @PreAuthorize("@preAuthServiceImplement.isRoomOwnerFromQuestion(#token,#idQuestion)==true")
    public ResponseEntity<ResponseDto> updateAnswersDisplayed(@RequestBody Long idQuestion, @RequestHeader (name="Authorization") String token) {
        try {
            ResponseDto dto = this.questionService.updateAnswersDisplayed(idQuestion);
            return new ResponseEntity<ResponseDto>(dto,HttpStatus.OK);
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Metoda pre vymazanie otazky pomocou DELETE requestu
     * Metoda je pouzita autorom pre zmazanie otazky
     * cesta:  /api/questions/delete/{id}
     * @param idQuestion ID otazky ktora ma byt vymazana (Long)
     * @return ResponseEntity<ResponseDto> Vracia ResponseDto so spravou o zmazani otazky
     *                                     Vracia null ak otazka nebola najdena
     */
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("@preAuthServiceImplement.isRoomOwnerFromQuestion(#token,#idQuestion)==true")
    public ResponseEntity<ResponseDto> deleteQuestion(@PathVariable("id") Long idQuestion, @RequestHeader (name="Authorization") String token) {
        try {
            ResponseDto dto = this.questionService.deleteQuestion(idQuestion);
            return new ResponseEntity<>(dto,HttpStatus.OK);
        } catch (EmptyResultDataAccessException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Metoda pre pridanie moznosti otazky pomocou POST requestu
     * Metoda je pouzita autorom pre pridanie moznosti k vytvorenej otazke
     * cesta:  /api/questions/options/add
     * @param optionalAnswerDto Pole moznosti pre otazku, ku ktorej maju byt monosti pridane
     *                          (OptionalAnswerDto[])
     * @return ResponseEntity<ResponseDto> Vracia ResponseDto so spravou o pridani moznosti
     *                                     Vracia null miesto ResponseDto ak otazka nebola najdena alebo
     *                                     niektora z moznosti bola prazdna
     */
    @PostMapping(value = "/options/add")
    @PreAuthorize("@preAuthServiceImplement.isRoomOwnerFromQuestion(#token,#optionalAnswerDto[0].idQuestion)==true")
    public ResponseEntity<ResponseDto> addOptionalAnswers(@RequestBody OptionalAnswerDto[] optionalAnswerDto, @RequestHeader (name="Authorization") String token) {
        try {
            ResponseDto dto = this.questionService.addOptionalAnswer(optionalAnswerDto);
            return new ResponseEntity<ResponseDto>(dto,HttpStatus.OK);
        } catch (IllegalArgumentException | NullPointerException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Metoda pre ziskanie moznosti otazky pomocou GET requestu
     * Metoda je pouzita pre zobrazenie moznosti k danej otazke
     * cesta:  /api/questions/get/{id}
     * @param idQuestion ID otazky, ktorej Set moznosti je pozadovany
     *                          (Long)
     * @return ResponseEntity<Set<OptionalAnswerDto>> Vracia Set moznosti otazky
     *                                                Vracia null miesto ResponseDto ak otazka
     *                                                nebola najdena
     */
    @GetMapping(value = "/options/get/{id}")
    @PreAuthorize("hasRole('Student') || hasRole('Vyucujuci')")
    public ResponseEntity<Set<OptionalAnswerDto>> getQuestionOptionalAnswers(@PathVariable("id") Long  idQuestion) {
        try
        {
            return new ResponseEntity<Set<OptionalAnswerDto>>(
                    this.questionService.getQuestionOptionalAnswers(idQuestion),HttpStatus.OK);
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }
}
