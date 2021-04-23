package sk.uniza.fri.askfri.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.model.Answer;
import sk.uniza.fri.askfri.model.Question;
import sk.uniza.fri.askfri.model.dto.AnswerDto;
import sk.uniza.fri.askfri.model.dto.ResponseDto;
import sk.uniza.fri.askfri.service.IAnswerService;
import sk.uniza.fri.askfri.service.IQuestionService;

import java.util.Set;
import java.util.stream.Collectors;

/** Sluzba pracujuca s Answer
 * implementuje IAnswerService
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@Service
public class AnswerServiceImplement implements IAnswerService {

    private final IQuestionService questionService;
    private final ModelMapper modelMapper;

    public AnswerServiceImplement(IQuestionService questionService, ModelMapper modelMapper) {
        this.questionService = questionService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseDto createAnswer(AnswerDto[] answerDto) {
        for (AnswerDto dto : answerDto) {
            if (dto.getContent().trim().equals("")) {
                throw new IllegalArgumentException("Prázdnu odpopveď sa nepodarilo odoslať");
            }
        }
        Question parentQuestion = this.questionService.findByIdQuestion(answerDto[0].getIdQuestion());
        if (parentQuestion != null)
        {
            for (AnswerDto ans : answerDto) {
                Answer answer = modelMapper.map(ans, Answer.class);
                    parentQuestion.addAnswer(answer);
            }
            this.questionService.saveQuestion(parentQuestion);
            return new ResponseDto(parentQuestion.getIdQuestion(), "Odpoveď bola odoslaná");
        }
        throw new NullPointerException("Otázka pre odpovede nebola nájdená");
    }


    /** Pokusi sa najst rodicovsku otazku a pokial bola najdena najde jej set odpovedi
     *  ktory premapuje na DTO
     * @param idQuestion ID otazky
     * @return Set<AnswerDto> set odpovedi pre danu otazku
     * @throws NullPointerException ak otazka nie je najdena
     */
    @Override
    public Set<AnswerDto> getAllQuestionAnswers(Long idQuestion) {
        Question parentQuestion = this.questionService.findByIdQuestion(idQuestion);
        if (parentQuestion != null)
        {
            return parentQuestion.getAnswersSet()
                    .stream()
                    .map(answer -> modelMapper.map(answer, AnswerDto.class))
                    .collect(Collectors.toSet());
        }
        throw new NullPointerException("Odpovede k otázke sa nepodarilo dostat. Otazka nebola najdena");
    }

}
