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
import sk.uniza.fri.askfri.model.dto.ResponseDto;
import sk.uniza.fri.askfri.service.IAnswerService;
import sk.uniza.fri.askfri.service.IQuestionService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/answers")
public class AnswerController {

    private final IQuestionService questionService;
    private final ModelMapper modelMapper;

    public AnswerController(IQuestionService questionService, ModelMapper modelMapper) {
        this.questionService = questionService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<ResponseDto> createAnswer(@RequestBody AnswerDto answerDto) {
        Question parentQuestion = this.questionService.findByIdQuestion(answerDto.getIdQuestion());
        Answer answer = modelMapper.map(answerDto, Answer.class);
        if (parentQuestion != null && !answerDto.getContent().equals("")) {
            parentQuestion.addAnswer(answer);
            this.questionService.saveQuestion(parentQuestion);
            return new ResponseEntity<>(new ResponseDto(answer.getIdAnswer(), "Odpoveď bola vytvorená"),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping(value = "/question/{id}")
    public ResponseEntity<Set<AnswerDto>> getAllQuestionAnswers(@PathVariable("id") long  idQuestion) {
        Question parentQuestion = this.questionService.findByIdQuestion(idQuestion);
        if (parentQuestion != null)
        {
            return new ResponseEntity<Set<AnswerDto>>(parentQuestion.getAnswersSet()
                    .stream()
                    .map(answer -> modelMapper.map(answer, AnswerDto.class))
                    .collect(Collectors.toSet()),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }
}
