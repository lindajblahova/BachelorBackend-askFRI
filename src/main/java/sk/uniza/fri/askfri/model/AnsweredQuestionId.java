package sk.uniza.fri.askfri.model;

import java.io.Serializable;
import java.util.Objects;

public class AnsweredQuestionId implements Serializable {
    private Long idUser;
    private Long idQuestion;

    public AnsweredQuestionId(User idUser, Question idQuestion) {
        this.idUser = idUser.getIdUser();
        this.idQuestion = idQuestion.getIdQuestion();
    }

    public AnsweredQuestionId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnsweredQuestionId that = (AnsweredQuestionId) o;
        return Objects.equals(idUser, that.idUser) &&
                Objects.equals(idQuestion, that.idQuestion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, idQuestion);
    }

    public Long getIdUser() {
        return idUser;
    }

    public Long getIdQuestion() {
        return idQuestion;
    }

}
