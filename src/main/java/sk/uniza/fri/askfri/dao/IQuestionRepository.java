package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.askfri.model.Question;
import sk.uniza.fri.askfri.model.Room;

import java.util.Set;

@Repository
public interface IQuestionRepository extends JpaRepository<Question, Long> {

    Question findQuestionByIdQuestion(Long idQuestion);

    @Modifying
    @Query("delete from question q where q.idRoom=?1")
    void deleteQuestionsByIdRoom_IdRoom(Room room);

    @Modifying
    @Query("delete from question q where q.idRoom.idRoom=?1")
    void deleteAllByIdRoom_IdRoom(Long idRoom);


}
















  /*  @EntityGraph(value = "roomQuestions", type = EntityGraph.EntityGraphType.LOAD)
    List<Question> findQuestionsByIdRoom(Room room);*/
