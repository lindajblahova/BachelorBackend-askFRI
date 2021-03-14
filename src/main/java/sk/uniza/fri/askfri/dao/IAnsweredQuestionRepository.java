package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.askfri.model.AnsweredQuestion;
import java.util.List;


@Repository
public interface IAnsweredQuestionRepository extends JpaRepository<AnsweredQuestion, Long> {
    Integer countAllByIdQuestion_IdQuestion(Long idQuestion) ;
    @Query(value = "SELECT idQuestion.idQuestion FROM answeredQuestion WHERE idUser.idUser=?1")
    List<Integer> selectAllUserQuestion(Long idUser);
}
