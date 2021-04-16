package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.askfri.model.Answer;
import sk.uniza.fri.askfri.model.Question;

import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import java.util.List;
import java.util.Set;

@Repository
public interface IAnswerRepository extends JpaRepository<Answer, Long> {

    /*@EntityGraph(value = "questionAnswers", type = EntityGraph.EntityGraphType.LOAD)
    Set<Answer> findAnswersByIdQuestion(Question question);*/

}
