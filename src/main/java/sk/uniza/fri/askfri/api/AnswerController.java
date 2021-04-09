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
@CrossOrigin(origins = "*")
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
    public ResponseEntity<AnswerDto> createAnswer(@RequestBody AnswerDto answerDto) {
        Question parentQuestion = this.questionService.findByIdQuestion(answerDto.getIdQuestion());
        Answer answer = modelMapper.map(answerDto, Answer.class);
        if (parentQuestion != null) {
            answer = this.answerService.saveAnswer(answer);
            AnswerDto dto = this.modelMapper.map(answer, AnswerDto.class);
            return new ResponseEntity<>(dto,HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/question/{id}")
    public List<AnswerDto> getAllQuestionAnswers(@PathVariable("id") long   idQuestion) {
        Question parentQuestion = this.questionService.findByIdQuestion(idQuestion);
        return this.answerService.findAnswersByIdQuestion(parentQuestion)
                .stream()
                .map( answer ->  modelMapper.map(answer, AnswerDto.class))
                .collect(Collectors.toList());
    }
}
