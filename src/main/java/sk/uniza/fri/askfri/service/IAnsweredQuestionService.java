package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.dto.AnsweredQuestionDto;
import sk.uniza.fri.askfri.model.dto.ResponseDto;

import java.util.Set;

public interface IAnsweredQuestionService {

    ResponseDto saveAnsweredQuestion(AnsweredQuestionDto answeredQuestionDto);
    Set<AnsweredQuestionDto> getAllUserQuestionAnswers(Long idUser);


}
