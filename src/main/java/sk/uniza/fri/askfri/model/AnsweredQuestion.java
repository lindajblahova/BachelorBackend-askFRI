package sk.uniza.fri.askfri.model;

import javax.persistence.*;

/** Trieda mapovana na tabulku answered_question databazy, sluzi pre
 *  zaznamenanie odpovede pouzivatela na otazku
 *  obsahuje rodicovskeho pouzivatela a rodicovsku otazku
 *  ID je trieda AnsweredQuestionId - KPK
 *  AnsweredQuestion je vo vztahu ManyToOne k rodicovskej otazke, referencuje ID otazky
 *  AnsweredQuestion je vo vztahu ManyToOne k rodicovskemu pouzivatelovi, referencuje
 *  ID pouzivatela
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@Entity(name = "answeredQuestion")
@Table(name = "answered_question")
@IdClass(AnsweredQuestionId.class)
public class AnsweredQuestion {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_profile_id_user", referencedColumnName = "idUser", nullable=false)
    private User idUser;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="question_id_question", referencedColumnName = "idQuestion", nullable=false)
    private Question idQuestion;

    public AnsweredQuestion() {
    }

    public AnsweredQuestion(User idUser, Question idQuestion) {
        this.idUser = idUser;
        this.idQuestion = idQuestion;
    }

    public Long getIdUser() {
        return idUser.getIdUser();
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    public Long getIdQuestion() {
        return idQuestion.getIdQuestion();
    }

    public void setIdQuestion(Question idQuestion) {
        this.idQuestion = idQuestion;
    }

    public Long getRoomFromQuestion() { return  this.idQuestion.getIdRoom();}
}
