package sk.uniza.fri.askfri.model.dto;

public class AnsweredQuestionDto {

    private Long idUser;
    private Long idQuestion;

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(Long idQuestion) {
        this.idQuestion = idQuestion;
    }
}
