package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.Answer;
import sk.uniza.fri.askfri.model.Question;

import java.util.List;

public interface IAnswerService {
    Answer saveAnswer(Answer answer);
    List<Answer> findAnswersByIdQuestion(Question idQuestion);
}
