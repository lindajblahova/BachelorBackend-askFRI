package sk.uniza.fri.askfri.api;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.model.Answer;
import sk.uniza.fri.askfri.model.AnsweredQuestion;
import sk.uniza.fri.askfri.model.Question;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.AnswerDto;
import sk.uniza.fri.askfri.model.dto.AnsweredQuestionDto;
import sk.uniza.fri.askfri.service.IAnsweredQuestionService;
import sk.uniza.fri.askfri.service.IQuestionService;
import sk.uniza.fri.askfri.service.IUserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/answered")
public class AnsweredQuestionController {

    private final ModelMapper modelMapper;
    private final IAnsweredQuestionService answeredQuestionService;
    private final IQuestionService questionService;
    private final IUserService userService;

    public AnsweredQuestionController(ModelMapper modelMapper, IAnsweredQuestionService answeredQuestionService, IQuestionService questionService, IUserService userService) {
        this.modelMapper = modelMapper;
        this.answeredQuestionService = answeredQuestionService;
        this.questionService = questionService;
        this.userService = userService;
    }

    @PostMapping(value = "/add")
    public ResponseEntity createAnsweredQuestion(@RequestBody AnsweredQuestionDto answeredQuestionDto) {
        Question parentQuestion = this.questionService.findByIdQuestion(answeredQuestionDto.getIdQuestion());
        User parentUser = this.userService.getUserByIdUser(answeredQuestionDto.getIdUser());
        AnsweredQuestion answeredQuestion = modelMapper.map(answeredQuestionDto, AnsweredQuestion.class);
        if (parentQuestion != null && parentUser != null) {
            this.answeredQuestionService.saveAnsweredQuestion(answeredQuestion);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/question")
    public Integer getQuestionUsersAnsweredCount(@RequestBody Long idQuestion) {
        Question parentQuestion = this.questionService.findByIdQuestion(idQuestion);
        return this.answeredQuestionService.questionAnswersCount(parentQuestion.getIdQuestion());
    }

    @GetMapping(value = "/user")
    public List<AnsweredQuestionDto> getAllQuestionAnswers(@RequestBody Long idUser) {
        User parentUser = this.userService.getUserByIdUser(idUser);
        return this.answeredQuestionService.userAnsweredQuestions(parentUser.getIdUser())
                .stream()
                .map( answeredQuestion ->  modelMapper.map(answeredQuestion, AnsweredQuestionDto.class))
                .collect(Collectors.toList());
    }
}
