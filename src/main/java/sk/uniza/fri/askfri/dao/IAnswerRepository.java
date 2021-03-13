package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.askfri.model.Answer;
import sk.uniza.fri.askfri.model.Question;

import java.util.List;

@Repository
public interface IAnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByIdQuestion(Question question);
}
