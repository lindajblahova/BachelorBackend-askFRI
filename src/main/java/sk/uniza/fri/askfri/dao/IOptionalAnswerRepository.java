package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import sk.uniza.fri.askfri.model.OptionalAnswer;
import sk.uniza.fri.askfri.model.Question;

import java.util.Set;

public interface IOptionalAnswerRepository extends JpaRepository<OptionalAnswer, Long> {

    @Modifying
    @Query("DELETE FROM optionalAnswer oa WHERE oa.idQuestion=?1")
    void deleteOptionalAnswersByIdQuestion_IdQuestion(Question question);

    @Modifying
    @Query("DELETE FROM optionalAnswer oa WHERE oa.idQuestion.idQuestion=?1")
    void deleteAllByIdQuestion_IdQuestion(Long idQuestion);


  /*  @EntityGraph(value = "questionOptionalAnswers", type = EntityGraph.EntityGraphType.LOAD)
    Set<OptionalAnswer> findOptionalAnswersByIdQuestion(Question question);*/
}
