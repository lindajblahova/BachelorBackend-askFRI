package sk.uniza.fri.askfri.api;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.model.Answer;
import sk.uniza.fri.askfri.model.Question;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.dto.AnswerDto;
import sk.uniza.fri.askfri.model.dto.QuestionDto;
import sk.uniza.fri.askfri.service.IAnswerService;
import sk.uniza.fri.askfri.service.IQuestionService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    private final IAnswerService answerService;
    private final IQuestionService questionService;
    private final ModelMapper modelMapper;

    public AnswerController(IAnswerService answerService, IQuestionService questionService, ModelMapper modelMapper) {
        this.answerService = answerService;
        this.questionService = questionService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/add")
    public ResponseEntity createAnswer(@RequestBody AnswerDto answerDto) {
        Question parentQuestion = this.questionService.findByIdQuestion(answerDto.getIdQuestion());
        Answer answer = modelMapper.map(answerDto, Answer.class);
        if (parentQuestion != null) {
            this.answerService.saveAnswer(answer);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/question")
    public List<AnswerDto> getAllQuestionAnswers(@RequestBody Long idQuestion) {
        Question parentQuestion = this.questionService.findByIdQuestion(idQuestion);
        return this.answerService.findAnswersByIdQuestion(parentQuestion)
                .stream()
                .map( answer ->  modelMapper.map(answer, AnswerDto.class))
                .collect(Collectors.toList());
    }
}
