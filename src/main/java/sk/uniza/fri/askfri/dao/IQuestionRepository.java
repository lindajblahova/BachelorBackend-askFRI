package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.askfri.model.Question;
import sk.uniza.fri.askfri.model.Room;

import java.util.List;

@Repository
public interface IQuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByIdRoom(Room room);
    Question findQuestionByIdQuestion(Long idQuestion);
}
