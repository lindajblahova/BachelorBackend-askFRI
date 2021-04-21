package sk.uniza.fri.askfri.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.askfri.model.dto.AnswerDto;
import sk.uniza.fri.askfri.model.dto.ResponseDto;
import sk.uniza.fri.askfri.service.IAnswerService;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/answers")
public class AnswerController {

    private final IAnswerService answerService;

    public AnswerController(IAnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping(value = "/question/{id}")
    public ResponseEntity<Set<AnswerDto>> getAllQuestionAnswers(@PathVariable("id") Long  idQuestion) {
        try {
            Set<AnswerDto> dto = this.answerService.getAllQuestionAnswers(idQuestion);
            return new ResponseEntity<>(dto,HttpStatus.OK);

        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/add")
    public ResponseEntity<ResponseDto> createAnswer(@RequestBody AnswerDto[] answerDto) {
        try {
            ResponseDto dto = this.answerService.createAnswer(answerDto);
            return new ResponseEntity<ResponseDto>(dto,HttpStatus.OK);

        } catch (NullPointerException e)
        {
            return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
