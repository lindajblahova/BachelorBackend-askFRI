package sk.uniza.fri.askfri.model;

import java.io.Serializable;
import java.util.Objects;

/** Trieda reprezentujuca ID pre AnsweredQuestion
 *  implementuje Serializable
 *  obsahuje ID rodicovskeho pouzivatela a ID rodicovskej otazky
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
public class AnsweredQuestionId implements Serializable {
    private Long idUser;
    private Long idQuestion;

    public AnsweredQuestionId(User idUser, Question idQuestion) {
        this.idUser = idUser.getIdUser();
        this.idQuestion = idQuestion.getIdQuestion();
    }

    public AnsweredQuestionId() {
    }

    public Long getIdUser() {
        return idUser;
    }

    public Long getIdQuestion() {
        return idQuestion;
    }

}
