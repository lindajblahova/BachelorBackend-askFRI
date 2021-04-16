package sk.uniza.fri.askfri.dao;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.askfri.model.AnsweredQuestion;
import sk.uniza.fri.askfri.model.AnsweredQuestionId;
import sk.uniza.fri.askfri.model.User;

import java.util.List;

@Repository
public interface IAnsweredQuestionRepository extends JpaRepository<AnsweredQuestion, AnsweredQuestionId> {

   /* @EntityGraph(value = "userAnsweredQuestions", type = EntityGraph.EntityGraphType.LOAD)
    List<AnsweredQuestion> findByIdUser(User user);*/

}
