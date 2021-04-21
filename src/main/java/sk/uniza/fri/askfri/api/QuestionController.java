package sk.uniza.fri.askfri.api;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.model.dto.*;
import sk.uniza.fri.askfri.service.IAnswerService;
import sk.uniza.fri.askfri.service.IAnsweredQuestionService;
import sk.uniza.fri.askfri.service.IQuestionService;

import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/questions")
public class QuestionController {

    private final IQuestionService questionService;
    private final IAnsweredQuestionService answeredQuestionService;
    private final IAnswerService answerService;

    public QuestionController(IQuestionService questionService, IAnsweredQuestionService answeredQuestionService, IAnswerService answerService) {
        this.questionService = questionService;
        this.answeredQuestionService = answeredQuestionService;
        this.answerService = answerService;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<QuestionDto> createQuestion(@RequestBody QuestionDto questionDto) {
        try {
            QuestionDto dto = this.questionService.createQuestion(questionDto);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (IllegalArgumentException e)
        {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping(value = "/room/{id}")
    public ResponseEntity<Set<QuestionDto>> getAllRoomQuestions(@PathVariable("id") Long idRoom) {
        try {
            return new ResponseEntity<Set<QuestionDto>>(this.questionService.findQuestionsByRoom(idRoom), HttpStatus.OK );
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/room/active/{id}")
    public ResponseEntity<Set<QuestionDto>> getRoomActiveQuestions(@PathVariable("id") Long idRoom) {
        try {
            return new ResponseEntity<Set<QuestionDto>>(this.questionService.findQuestionsByRoomAndActive(idRoom), HttpStatus.OK );
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update/question")
    public ResponseEntity<ResponseDto> updateQuestionDisplayed(@RequestBody Long idQuestion) {
        try {
            ResponseDto dto = this.questionService.updateQuestionDisplayed(idQuestion);
            return new ResponseEntity<ResponseDto>(dto,HttpStatus.OK);
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping(value = "/update/answers")
    public ResponseEntity<ResponseDto> updateAnswersDisplayed(@RequestBody Long idQuestion) {
        try {
            ResponseDto dto = this.questionService.updateAnswersDisplayed(idQuestion);
            return new ResponseEntity<ResponseDto>(dto,HttpStatus.OK);
        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<ResponseDto> deleteQuestion(@PathVariable("id") Long idQuestion) {
        try {
            ResponseDto dto = this.questionService.deleteQuestion(idQuestion);
            return new ResponseEntity<>(dto,HttpStatus.OK);
        } catch (EmptyResultDataAccessException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/options/add")
    public ResponseEntity<ResponseDto> addOptionalAnswers(@RequestBody OptionalAnswerDto[] optionalAnswerDto) {
        try {
            ResponseDto dto = this.questionService.addOptionalAnswer(optionalAnswerDto);
            return new ResponseEntity<ResponseDto>(dto,HttpStatus.OK);
        } catch (IllegalArgumentException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping(value = "/options/get/{id}")
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
