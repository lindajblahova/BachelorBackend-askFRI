package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.OptionalAnswer;
import sk.uniza.fri.askfri.model.Question;

public interface IQuestionService {
    Question saveQuestion(Question question);
    void deleteQuestion(Long idQuestion);
    Question findByIdQuestion(Long idQuestion);

    OptionalAnswer saveOptionalAnswer(OptionalAnswer optionalAnswer);

   // void deleteQuestionsByRoom(Long idRoom);
}
