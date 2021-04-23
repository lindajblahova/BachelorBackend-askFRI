package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.askfri.model.Question;

/** Repozitar pre CRUD operacie s tabulkou question
 * extends JpaRepository, pre typ Question a ID typu Long
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@Repository
public interface IQuestionRepository extends JpaRepository<Question, Long> {

    /** Metoda pre ziskanie otazky podla ID otazky
     * @param idQuestion (Long) ID otazky
     * @return Question najdena otazka/null
     */
    Question findQuestionByIdQuestion(Long idQuestion);

    /** Metoda pre zistenie ci je pouzivatel autorom miestnosti do ktorej otazka patri
     * @param idUser (Long) ID pouzivatela
     * @param idQuestion (Long) ID otazky
     * @return Integer pocet riadkov najdenych podla podmienky
     */
    @Query("SELECT count(r.idOwner.idUser) FROM question q JOIN room r ON r.idRoom=q.idRoom.idRoom " +
            "WHERE r.idOwner.idUser=?1 AND r.idRoom IN " +
            "(SELECT q.idRoom.idRoom FROM question q WHERE q.idQuestion=?2)")
    Integer isUserRoomOwner(Long idUser, Long idQuestion);
}
















  /*  @EntityGraph(value = "roomQuestions", type = EntityGraph.EntityGraphType.LOAD)
    List<Question> findQuestionsByIdRoom(Room room);*/
