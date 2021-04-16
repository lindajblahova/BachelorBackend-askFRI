package sk.uniza.fri.askfri.model.dto;

public class AnsQuestionClass {

    private Long idUser;
    private Long idQuestion;

    public AnsQuestionClass(Long idUser, Long idQuestion) {
        this.idUser = idUser;
        this.idQuestion = idQuestion;
    }

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
