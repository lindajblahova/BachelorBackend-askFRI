package sk.uniza.fri.askfri.model;

import javax.persistence.*;

@Entity(name = "answeredQuestion")
@Table(name = "answered_question")
public class AnsweredQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idAnsweredQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_user", nullable=false)
    private User idUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_question", nullable=false)
    private Question idQuestion;

    public AnsweredQuestion(User user, Question question) {
        this.idUser = user;
        this.idQuestion = question;
    }

    public AnsweredQuestion() {}


    public Long getIdUser() {
        return idUser.getIdUser();
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    public Long getIdAnsweredQuestion() {
        return idAnsweredQuestion;
    }

    public void setIdAnsweredQuestion(Long idAnsweredQuestion) {
        this.idAnsweredQuestion = idAnsweredQuestion;
    }

    public Long getIdQuestion() {
        return idQuestion.getIdQuestion();
    }

    public void setIdQuestion(Question idQuestion) {
        this.idQuestion = idQuestion;
    }
}
