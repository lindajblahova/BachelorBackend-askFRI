package sk.uniza.fri.askfri.model.dto;

/** Trieda DTO udajov pre odpoved na otazku
 * obsahuje ID odpovede, ID otazky a obsah odpovede
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
public class AnswerDto {

    private Long idAnswer;
    private Long idQuestion;
    private String content;

    public Long getIdAnswer() {
        return idAnswer;
    }

    public void setIdAnswer(Long idAnswer) {
        this.idAnswer = idAnswer;
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
