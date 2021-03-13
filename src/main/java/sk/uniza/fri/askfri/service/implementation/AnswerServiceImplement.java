package sk.uniza.fri.askfri.service.implementation;

import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.IAnswerRepository;
import sk.uniza.fri.askfri.model.Answer;
import sk.uniza.fri.askfri.model.Question;
import sk.uniza.fri.askfri.service.IAnswerService;

import java.util.List;

@Service
public class AnswerServiceImplement implements IAnswerService {

    private final IAnswerRepository answerRepository;

    public AnswerServiceImplement(IAnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }


    @Override
    public Answer saveAnswer(Answer answer) {
        return this.answerRepository.save(answer);
    }

    @Override
    public List<Answer> findAnswersByIdQuestion(Question idQuestion) {
        return this.answerRepository.findAllByIdQuestion(idQuestion);
    }
}
