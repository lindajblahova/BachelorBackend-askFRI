package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.askfri.model.AnsweredQuestion;
import sk.uniza.fri.askfri.model.dto.AnsweredQuestionDto;

import java.util.List;


@Repository
public interface IAnsweredQuestionRepository extends JpaRepository<AnsweredQuestion, Long> {
    Integer countAllByIdQuestion_IdQuestion(Long idQuestion) ;

    //@Query(value = "SELECT idQuestion FROM answeredQuestion WHERE idUser=?1")
    List<AnsweredQuestion> findAllByIdUser_IdUser(Long idUser);
}
