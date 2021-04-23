package sk.uniza.fri.askfri.model.dto;

/** Trieda DTO udajov pre zaznam pouzivatela a zopovedanej otazky
 * obsahuje ID pouzivatela, ID otazky
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
public class AnsweredQuestionDto {

    private Long idUser;
    private Long idQuestion;

    public AnsweredQuestionDto(Long idUser, Long idQuestion) {
        this.idUser = idUser;
        this.idQuestion = idQuestion;
    }

    public AnsweredQuestionDto() {
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
