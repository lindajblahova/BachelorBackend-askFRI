package sk.uniza.fri.askfri.model;

import javax.persistence.*;

@Entity(name = "answeredQuestion")
@Table(name = "answered_question")
@IdClass(AnsweredQuestionId.class)
public class AnsweredQuestion {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_profile_id_user", referencedColumnName = "idUser", nullable=false, updatable = false)
    private User idUser;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="question_id_question", referencedColumnName = "idQuestion", nullable=false, updatable = false)
    private Question idQuestion;

    public AnsweredQuestion() {
    }

    public AnsweredQuestion(User idUser, Question idQuestion) {
        this.idUser = idUser;
        this.idQuestion = idQuestion;
    }

    public User getIdUser() {
        return idUser;
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    public Question getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(Question idQuestion) {
        this.idQuestion = idQuestion;
    }
}
