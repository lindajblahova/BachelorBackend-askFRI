package sk.uniza.fri.askfri.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.IAnswerRepository;
import sk.uniza.fri.askfri.model.Answer;
import sk.uniza.fri.askfri.model.Question;
import sk.uniza.fri.askfri.model.dto.AnswerDto;
import sk.uniza.fri.askfri.model.dto.ResponseDto;
import sk.uniza.fri.askfri.service.IAnswerService;
import sk.uniza.fri.askfri.service.IQuestionService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AnswerServiceImplement implements IAnswerService {

    private final IAnswerRepository answerRepository;
    private final IQuestionService questionService;
    private final ModelMapper modelMapper;

    public AnswerServiceImplement(IAnswerRepository answerRepository, IQuestionService questionService,
                                  ModelMapper modelMapper) {
        this.answerRepository = answerRepository;
        this.questionService = questionService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseDto createAnswer(AnswerDto[] answerDto) {
        Question parentQuestion = this.questionService.findByIdQuestion(answerDto[0].getIdQuestion());
        if (parentQuestion != null)
        {
            for (AnswerDto ans : answerDto) {
                Answer answer = modelMapper.map(ans, Answer.class);
                if (!answer.getContent().trim().equals("")) {
                    parentQuestion.addAnswer(answer);
                }
            }
            this.questionService.saveQuestion(parentQuestion);
            return new ResponseDto(parentQuestion.getIdQuestion(), "Odpoveď bola odoslaná");
        }
        throw new NullPointerException("Odpoveď sa nepodarilo odoslať");
    }

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
        throw new NullPointerException("Odpovede k otázke neboli nájdené");
    }

}
