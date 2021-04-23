package sk.uniza.fri.askfri.api;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.model.dto.AnswerDto;
import sk.uniza.fri.askfri.model.dto.AnsweredQuestionDto;
import sk.uniza.fri.askfri.model.dto.ResponseDto;
import sk.uniza.fri.askfri.service.IAnswerService;
import sk.uniza.fri.askfri.service.IAnsweredQuestionService;

import java.util.Set;

/**
 * Controller - endpoint pre pracu s odpovedami otázok
 * pre pristup je podmienka platneho jwt
 * cesta: /api/answers/*
 *
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/answers")
@PreAuthorize("isAuthenticated()")
public class AnswerController {

    private final IAnswerService answerService;
    private final IAnsweredQuestionService answeredQuestionService;

    public AnswerController(IAnswerService answerService, IAnsweredQuestionService answeredQuestionService) {
        this.answerService = answerService;
        this.answeredQuestionService = answeredQuestionService;
    }

    /**
     * Metoda pre pristup k zoznamu vsetkych odpovedi k pozadovanej otazke pomocou GET requestu
     * cesta:  /api/answers/question/{id}
     * @param idQuestion Id otazky pre ktoru su pozadovane odpovede (Long)
     * @return ResponseEntity<Set<AnswerDto>> Vracia set vsetkych odpovedi k danej otazke
     *                                        Vracia null ak otazka nebola najdena
     */
    @GetMapping(value = "/question/{id}")
    @PreAuthorize("hasRole('Student') || hasRole('Vyucujuci')")
    public ResponseEntity<Set<AnswerDto>> getAllQuestionAnswers(@PathVariable("id") Long  idQuestion) {
        try {
            Set<AnswerDto> dto = this.answerService.getAllQuestionAnswers(idQuestion);
            return new ResponseEntity<>(dto,HttpStatus.OK);

        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Metoda pre vytvorenie odpovedi k otazke pomocou POST requestu
     * cesta:  /api/answers/add
     * @param answerDto Pole vsetkych odpovedi ku zodpovedanej otazke
     *                  (pri type otazky slider a radio pole obsahuje len 1 prvok,
     *                  pri type checkbox obsahuje vsetky odpovede, ktore pouzivatel oznacil
     *                  je to urobene polom kvoli tomu, aby pri checkboxe nemuseli byt odpovede
     *                  odosielane v niekolkych samostatnych requestoch (AnswerDto[])
     * @return ResponseEntity<ResponseDto> Pokial boli odpovede ulozene, vrati ResponseDto
     *                                     so spravou o ulozeni
     *                                     Vracia null miesto ResponseDto ak otazka nebola najdena alebo
     *                                     odpovede boli prazdne
     */
    @PostMapping(value = "/add/{id}")
    @PreAuthorize("hasRole('Student') || hasRole('Vyucujuci')")
    public ResponseEntity<ResponseDto> createAnswer(@RequestBody AnswerDto[] answerDto, @PathVariable("id") Long idUser) {
        try {
            ResponseDto dto = this.answerService.createAnswer(answerDto);
            try
                {
                    AnsweredQuestionDto aqDto = new AnsweredQuestionDto(idUser, answerDto[0].getIdQuestion());
                    ResponseDto dto1 = this.answeredQuestionService.saveAnsweredQuestion(aqDto);
                    return new ResponseEntity<ResponseDto>(dto,HttpStatus.OK);
                } catch (NullPointerException e) {
                    return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
                }
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
