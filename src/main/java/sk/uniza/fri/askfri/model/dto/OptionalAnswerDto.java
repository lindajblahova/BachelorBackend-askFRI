package sk.uniza.fri.askfri.model.dto;

/** Trieda DTO udajov pre moznost otazky
 * obsahuje ID moznosti, ID otazky a jej obsah
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
public class OptionalAnswerDto {

    private Long idOptionalAnswer;
    private Long idQuestion;
    private String content;


    public Long getIdOptionalAnswer() {
        return idOptionalAnswer;
    }

    public void setIdOptionalAnswer(Long idOptionalAnswer) {
        this.idOptionalAnswer = idOptionalAnswer;
    }

    public Long getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(Long idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
