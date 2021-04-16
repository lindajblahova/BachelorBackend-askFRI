package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.askfri.model.Message;
import sk.uniza.fri.askfri.model.Question;
import sk.uniza.fri.askfri.model.Room;

import java.util.List;

@Repository
public interface IQuestionRepository extends JpaRepository<Question, Long> {

  /*  @EntityGraph(value = "roomQuestions", type = EntityGraph.EntityGraphType.LOAD)
    List<Question> findQuestionsByIdRoom(Room room);*/

    Question findQuestionByIdQuestion(Long idQuestion);

  /*  @Modifying
    @Query("delete from question q where q.idRoom.idRoom=?1")
    void deleteQuestionsByIdRoom_IdRoom(Long idRoom);*/
}
