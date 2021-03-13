package sk.uniza.fri.askfri.service.implementation;

import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.IAnsweredQuestionRepository;
import sk.uniza.fri.askfri.model.AnsweredQuestion;
import sk.uniza.fri.askfri.model.dto.AnsweredQuestionDto;
import sk.uniza.fri.askfri.service.IAnsweredQuestionService;

import java.util.List;

@Service
public class AnsweredQuestionServiceImplement implements IAnsweredQuestionService {

    private final IAnsweredQuestionRepository answeredQuestionRepository;

    public AnsweredQuestionServiceImplement(IAnsweredQuestionRepository answeredQuestionRepository) {
        this.answeredQuestionRepository = answeredQuestionRepository;
    }

    @Override
    public AnsweredQuestion saveAnsweredQuestion(AnsweredQuestion answeredQuestion) {
        return this.answeredQuestionRepository.save(answeredQuestion);
    }

    @Override
    public Integer questionAnswersCount(Long idQuestion) {
        return this.answeredQuestionRepository.countAllByIdQuestion_IdQuestion(idQuestion);
    }

    @Override
    public List<AnsweredQuestion> userAnsweredQuestions(Long idUser) {
        return this.answeredQuestionRepository.findAllByIdUser_IdUser(idUser);
    }
}
