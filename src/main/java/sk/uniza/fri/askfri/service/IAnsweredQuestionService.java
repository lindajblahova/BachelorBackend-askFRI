package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.AnsweredQuestion;
import sk.uniza.fri.askfri.model.dto.AnsweredQuestionDto;

import java.util.List;

public interface IAnsweredQuestionService {
    AnsweredQuestion saveAnsweredQuestion(AnsweredQuestion answeredQuestion);
    Integer questionAnswersCount(Long idQuestion);
    List<Integer> userAnsweredQuestions(Long idUser);
}
