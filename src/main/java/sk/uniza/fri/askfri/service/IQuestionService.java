package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.AnsweredQuestion;
import sk.uniza.fri.askfri.model.OptionalAnswer;
import sk.uniza.fri.askfri.model.Question;
import sk.uniza.fri.askfri.model.Room;

import java.util.List;

public interface IQuestionService {
    List<Question> findAllRoomQuestions(Room room);
    Question saveQuestion(Question question);
    void deleteQuestion(Long idQuestion);
    Question findByIdQuestion(Long idQuestion);

    OptionalAnswer saveOptionalAnswer(OptionalAnswer optionalAnswer);
}
