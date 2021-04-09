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
import sk.uniza.fri.askfri.service.IQuestionService;
import sk.uniza.fri.askfri.service.IRoomService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/questions")
public class QuestionController {

    private final IQuestionService questionService;
    private final IRoomService roomService;
    private final ModelMapper modelMapper;


    public QuestionController(IQuestionService questionService, IRoomService roomService, ModelMapper modelMapper) {
        this.questionService = questionService;
        this.roomService = roomService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<QuestionDto> createQuestion(@RequestBody QuestionDto questionDto) {
        Room parentRoom = this.roomService.findByIdRoom(questionDto.getIdRoom());
        Question question = modelMapper.map(questionDto, Question.class);
        if (parentRoom != null) {
            Question quest =  this.questionService.saveQuestion(question);
            QuestionDto dto = modelMapper.map(quest, QuestionDto.class);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/room/{id}")
    public List<QuestionDto> getAllRoomQuestions(@PathVariable("id") long idRoom) {
        Room parentRoom = this.roomService.findByIdRoom(idRoom);
        return this.questionService.findAllRoomQuestions(parentRoom)
                .stream()
                .map( message ->  modelMapper.map(message, QuestionDto.class))
                .collect(Collectors.toList());
    }

    @PutMapping(value = "/update/question")
    public ResponseEntity<QuestionDto> updateQuestionDisplayed(@RequestBody Long idQuestion) {
        Question foundQuestion = this.questionService.findByIdQuestion(idQuestion);
        if (foundQuestion.getIdQuestion() != null) {
            foundQuestion.setQuestionDisplayed(!foundQuestion.isQuestionDisplayed());
            foundQuestion = this.questionService.saveQuestion(foundQuestion);
            QuestionDto dto = this.modelMapper.map(foundQuestion, QuestionDto.class);
            return new ResponseEntity<>(dto,HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/update/answers")
    public ResponseEntity<QuestionDto> updateAnswersDisplayed(@RequestBody Long idQuestion) {
        Question foundQuestion = this.questionService.findByIdQuestion(idQuestion);
        if (foundQuestion.getIdQuestion() != null) {
            foundQuestion.setAnswersDisplayed(!foundQuestion.isAnswersDisplayed());
            foundQuestion = this.questionService.saveQuestion(foundQuestion);
            QuestionDto dto = this.modelMapper.map(foundQuestion, QuestionDto.class);
            return new ResponseEntity<>(dto,HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<QuestionDto> deleteQuestion(@PathVariable("id") long idQuestion) {
        this.questionService.deleteQuestion(idQuestion);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }


    @PostMapping(value = "/options/add")
    public ResponseEntity<OptionalAnswerDto> addOptionalAnswer(@RequestBody OptionalAnswerDto optionalAnswerDto) {
        Question parentQuestion = this.questionService.findByIdQuestion(optionalAnswerDto.getIdQuestion());
        if (parentQuestion != null) {
            OptionalAnswer optionalAnswer = this.modelMapper.map( optionalAnswerDto, OptionalAnswer.class);
            optionalAnswer = this.questionService.saveOptionalAnswer(optionalAnswer);
            OptionalAnswerDto dto = this.modelMapper.map(optionalAnswer, OptionalAnswerDto.class);
            return new ResponseEntity<>(dto,HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/options/get/{id}")
    public Set<OptionalAnswer> getQuestionOptionalAnswers(@PathVariable("id") long  idQuestion) {
        Question parentQuestion = this.questionService.findByIdQuestion(idQuestion);
        if (parentQuestion != null) {
            return  parentQuestion.getOptionalAnswerSet();
        }
        return null;
    }

    @GetMapping(value = "/answered/count")
    public Integer getQuestionUsersAnsweredCount(@RequestBody Long idQuestion) {
        Question parentQuestion = this.questionService.findByIdQuestion(idQuestion);
        return parentQuestion.getAnsweredQuestionSet().size();
    }
}
