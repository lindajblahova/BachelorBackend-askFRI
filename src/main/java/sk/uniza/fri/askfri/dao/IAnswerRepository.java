package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.askfri.model.Answer;
import sk.uniza.fri.askfri.model.Question;
import sk.uniza.fri.askfri.service.implementation.AnswerServiceImplement;

import java.util.Set;

@Repository
public interface IAnswerRepository extends JpaRepository<Answer, Long> {
    @Modifying
    @Query("DELETE FROM answer a WHERE a.idQuestion=?1")
    void deleteAnswersByIdQuestion_IdQuestion(Question question);

    @Modifying
    @Query("DELETE FROM answer a WHERE a.idQuestion.idQuestion=?1")
    void deleteAllByIdQuestion_IdQuestion(Long idQuestion);

}
