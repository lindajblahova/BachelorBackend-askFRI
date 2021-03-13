package sk.uniza.fri.askfri.service.implementation;

import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.IQuestionRepository;
import sk.uniza.fri.askfri.model.Question;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.service.IQuestionService;

import java.util.List;

@Service
public class QuestionServiceImplement implements IQuestionService {

    private final IQuestionRepository questionRepository;

    public QuestionServiceImplement(IQuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public List<Question> findAllRoomQuestions(Room room) {
        return this.questionRepository.findAllByIdRoom(room);
    }

    @Override
    public Question saveQuestion(Question question) {
        return this.questionRepository.save(question);
    }

    @Override
    public void deleteQuestion(Long idQuestion) {
        this.questionRepository.deleteById(idQuestion);
    }

    @Override
    public Question findByIdQuestion(Long idQuestion) {
        return this.questionRepository.findQuestionByIdQuestion(idQuestion);
    }
}
