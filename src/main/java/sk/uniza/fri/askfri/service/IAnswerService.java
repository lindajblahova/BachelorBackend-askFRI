package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.dto.AnswerDto;
import sk.uniza.fri.askfri.model.dto.ResponseDto;

import java.util.Set;

public interface IAnswerService {
    ResponseDto createAnswer(AnswerDto[] answerDto);
    Set<AnswerDto> getAllQuestionAnswers(Long  idQuestion);

}
