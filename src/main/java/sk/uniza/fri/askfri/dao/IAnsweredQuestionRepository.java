package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.askfri.model.AnsweredQuestion;
import sk.uniza.fri.askfri.model.AnsweredQuestionId;

/** Repozitar pre CRUD operacie s tabulkou answered_question
 * extends JpaRepository, pre typ AnsweredQuestion a ID typu (KPK) AnsweredQuestionId
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@Repository
public interface IAnsweredQuestionRepository extends JpaRepository<AnsweredQuestion, AnsweredQuestionId> {

}
