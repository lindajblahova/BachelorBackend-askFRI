package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import sk.uniza.fri.askfri.model.OptionalAnswer;
import sk.uniza.fri.askfri.model.Question;
import sk.uniza.fri.askfri.model.Room;

import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import java.util.List;
import java.util.Set;

public interface IOptionalAnswerRepository extends JpaRepository<OptionalAnswer, Long> {

  /*  @EntityGraph(value = "questionOptionalAnswers", type = EntityGraph.EntityGraphType.LOAD)
    Set<OptionalAnswer> findOptionalAnswersByIdQuestion(Question question);*/
}
