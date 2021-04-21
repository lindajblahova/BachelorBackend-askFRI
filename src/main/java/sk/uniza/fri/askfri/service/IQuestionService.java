package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.Question;
import sk.uniza.fri.askfri.model.dto.OptionalAnswerDto;
import sk.uniza.fri.askfri.model.dto.QuestionDto;
import sk.uniza.fri.askfri.model.dto.ResponseDto;

import java.util.Set;

public interface IQuestionService {
    QuestionDto createQuestion(QuestionDto questionDto);
    Question saveQuestion(Question question);
    Question saveQuestionFlush(Question question);
    ResponseDto deleteQuestion(Long idQuestion);
    Question findByIdQuestion(Long idQuestion);
    Set<QuestionDto> findQuestionsByRoom(Long idRoom);
    Set<QuestionDto> findQuestionsByRoomAndActive(Long idRoom);
    ResponseDto updateQuestionDisplayed(Long idQuestion);
    ResponseDto updateAnswersDisplayed(Long idQuestion);

    ResponseDto addOptionalAnswer(OptionalAnswerDto[] optionalAnswerDto);
    Set<OptionalAnswerDto> getQuestionOptionalAnswers(Long  idQuestion);


   // void deleteQuestionsByRoom(Long idRoom);
}
