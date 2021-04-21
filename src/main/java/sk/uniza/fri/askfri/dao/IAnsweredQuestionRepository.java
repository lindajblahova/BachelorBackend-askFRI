package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.askfri.model.AnsweredQuestion;
import sk.uniza.fri.askfri.model.AnsweredQuestionId;
import sk.uniza.fri.askfri.model.Question;

import java.util.Set;

@Repository
public interface IAnsweredQuestionRepository extends JpaRepository<AnsweredQuestion, AnsweredQuestionId> {

   /* @EntityGraph(value = "userAnsweredQuestions", type = EntityGraph.EntityGraphType.LOAD)
    List<AnsweredQuestion> findByIdUser(User user);*/

    @Modifying
    @Query("DELETE FROM answeredQuestion aq WHERE aq.idQuestion.idQuestion=?1")
    void deleteAllByIdQuestion_IdQuestion(Long idQuestion);

    @Modifying
    @Query("DELETE FROM answeredQuestion aq WHERE aq.idUser.idUser=?1")
    void deleteAllByIdUser_IdUser(Long idUser);

}
