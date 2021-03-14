package sk.uniza.fri.askfri.api;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.model.OptionalAnswer;
import sk.uniza.fri.askfri.model.Question;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.dto.OptionalAnswerDto;
import sk.uniza.fri.askfri.model.dto.QuestionDto;
import sk.uniza.fri.askfri.service.IOptionalAnswerService;
import sk.uniza.fri.askfri.service.IQuestionService;
import sk.uniza.fri.askfri.service.IRoomService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final IQuestionService questionService;
    private final IRoomService roomService;
    private final ModelMapper modelMapper;
    private final IOptionalAnswerService optionalAnswerService;

    public QuestionController(IQuestionService questionService, IRoomService roomService, ModelMapper modelMapper, IOptionalAnswerService optionalAnswerService) {
        this.questionService = questionService;
        this.roomService = roomService;
        this.modelMapper = modelMapper;
        this.optionalAnswerService = optionalAnswerService;
    }

    @PostMapping(value = "/add")
    public ResponseEntity createQuestion(@RequestBody QuestionDto questionDto) {
        Room parentRoom = this.roomService.findByIdRoom(questionDto.getIdRoom());
        Question question = modelMapper.map(questionDto, Question.class);
        if (parentRoom != null) {
            this.questionService.saveQuestion(question);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/room")
    public List<QuestionDto> getAllRoomQuestions(@RequestBody Long idRoom) {
        Room parentRoom = this.roomService.findByIdRoom(idRoom);
        return this.questionService.findAllRoomQuestions(parentRoom)
                .stream()
                .map( message ->  modelMapper.map(message, QuestionDto.class))
                .collect(Collectors.toList());
    }

    @PutMapping(value = "/update/question")
    public ResponseEntity updateQuestionDisplayed(@RequestBody Long idQuestion) {
        Question foundQuestion = this.questionService.findByIdQuestion(idQuestion);
        if (foundQuestion.getIdQuestion() != null) {
            if (foundQuestion.isAnswersDisplayed() && foundQuestion.isQuestionDisplayed()) {
                foundQuestion.setAnswersDisplayed(!foundQuestion.isAnswersDisplayed());
            }
            foundQuestion.setQuestionDisplayed(!foundQuestion.isQuestionDisplayed());
            this.questionService.saveQuestion(foundQuestion);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/update/answers")
    public ResponseEntity updateAnswersDisplayed(@RequestBody Long idQuestion) {
        Question foundQuestion = this.questionService.findByIdQuestion(idQuestion);
        if (foundQuestion.getIdQuestion() != null) {
            if (!foundQuestion.isQuestionDisplayed()) {
                foundQuestion.setQuestionDisplayed(!foundQuestion.isQuestionDisplayed());
            }
            foundQuestion.setAnswersDisplayed(!foundQuestion.isAnswersDisplayed());
            this.questionService.saveQuestion(foundQuestion);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity deleteQuestion(@RequestBody Long idQuestion) {
        this.questionService.deleteQuestion(idQuestion);
        return new ResponseEntity(HttpStatus.OK);
    }


    @PostMapping(value = "/options/add")
    public ResponseEntity addOptionalAnswer(@RequestBody OptionalAnswerDto optionalAnswerDto) {
        Question parentQuestion = this.questionService.findByIdQuestion(optionalAnswerDto.getIdQuestion());
        if (parentQuestion != null) {
            OptionalAnswer optionalAnswer = this.modelMapper.map( optionalAnswerDto, OptionalAnswer.class);
            this.optionalAnswerService.saveOptionalAnswer(optionalAnswer);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/options/get")
    public Set<OptionalAnswer> addOptionalAnswer(@RequestBody Long idQuestion) {
        Question parentQuestion = this.questionService.findByIdQuestion(idQuestion);
        if (parentQuestion != null) {
            return  parentQuestion.getOptionalAnswerSet();
        }
        return null;
    }
}
