package sk.uniza.fri.askfri.api;

import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.model.OptionalAnswer;
import sk.uniza.fri.askfri.model.Question;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.dto.MessageDto;
import sk.uniza.fri.askfri.model.dto.OptionalAnswerDto;
import sk.uniza.fri.askfri.model.dto.QuestionDto;
import sk.uniza.fri.askfri.model.dto.ResponseDto;
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
        if (parentRoom != null && !questionDto.getContent().equals("") &&
                (questionDto.getType() < 4 && questionDto.getType() > -1) ) {
            question =  this.questionService.saveQuestion(question);
            parentRoom.getQuestionSet().add(question);
            return new ResponseEntity<>(this.modelMapper.map(question, QuestionDto.class), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping(value = "/room/{id}")
    public ResponseEntity<Set<QuestionDto>> getAllRoomQuestions(@PathVariable("id") long idRoom) {
        Room parentRoom = this.roomService.findByIdRoom(idRoom);
        if (parentRoom != null) {
            return new ResponseEntity<Set<QuestionDto>>(parentRoom.getQuestionSet()
                    .stream()
                    .map(question -> modelMapper.map(question, QuestionDto.class))
                    .collect(Collectors.toSet()), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/update/question")
    public ResponseEntity<ResponseDto> updateQuestionDisplayed(@RequestBody Long idQuestion) {
        Question foundQuestion = this.questionService.findByIdQuestion(idQuestion);
        if (foundQuestion.getIdQuestion() != null) {
            foundQuestion.setQuestionDisplayed(!foundQuestion.isQuestionDisplayed());
            foundQuestion = this.questionService.saveQuestion(foundQuestion);
            return new ResponseEntity<ResponseDto>(new ResponseDto(foundQuestion.getIdQuestion(),
                    foundQuestion.isQuestionDisplayed() ? "Otázka je zobrazená" :
                            "Otázka nie je zobrazená" ),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
    }

    @PutMapping(value = "/update/answers")
    public ResponseEntity<ResponseDto> updateAnswersDisplayed(@RequestBody Long idQuestion) {
        Question foundQuestion = this.questionService.findByIdQuestion(idQuestion);
        if (foundQuestion.getIdQuestion() != null) {
            foundQuestion.setAnswersDisplayed(!foundQuestion.isAnswersDisplayed());
            foundQuestion = this.questionService.saveQuestion(foundQuestion);
            return new ResponseEntity<ResponseDto>(new ResponseDto(foundQuestion.getIdQuestion(),
                    foundQuestion.isAnswersDisplayed() ? "Výsledky sú zobrazené" :
                            "Výsledky nie sú zobrazené" ),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<ResponseDto> deleteQuestion(@PathVariable("id") long idQuestion) {
        try {
            Question question = this.questionService.findByIdQuestion(idQuestion);
            Long parentRoomId = question.getIdRoom();
            Room parentRoom = this.roomService.findByIdRoom(parentRoomId);
            question.getOptionalAnswerSet().clear();
            parentRoom.getQuestionSet().remove(question);
            this.questionService.deleteQuestion(idQuestion);
            return new ResponseEntity<>(new ResponseDto(idQuestion, "Otázka bola vymazaná"),HttpStatus.OK);
        } catch (EmptyResultDataAccessException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/options/add")
    public ResponseEntity<ResponseDto> addOptionalAnswer(@RequestBody OptionalAnswerDto optionalAnswerDto) {
        Question parentQuestion = this.questionService.findByIdQuestion(optionalAnswerDto.getIdQuestion());
        if (parentQuestion != null && !optionalAnswerDto.getContent().equals("")) {
            OptionalAnswer optionalAnswer = this.modelMapper.map( optionalAnswerDto, OptionalAnswer.class);
            optionalAnswer = this.questionService.saveOptionalAnswer(optionalAnswer);
            parentQuestion.getOptionalAnswerSet().add(optionalAnswer);
            return new ResponseEntity<ResponseDto>(new ResponseDto(optionalAnswer.getIdOptionalAnswer(), "Možnosť bola vytvorená"),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping(value = "/options/get/{id}")
    public ResponseEntity<Set<OptionalAnswer>> getQuestionOptionalAnswers(@PathVariable("id") long  idQuestion) {
        Question parentQuestion = this.questionService.findByIdQuestion(idQuestion);
        if (parentQuestion != null) {
            return new ResponseEntity<Set<OptionalAnswer>>(parentQuestion.getOptionalAnswerSet(),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }
}
