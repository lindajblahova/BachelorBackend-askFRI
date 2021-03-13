package sk.uniza.fri.askfri.model.dto;

public class QuestionDto {

    private Long idQuestion;
    private Long idRoom;
    private Integer type;
    private String content;
    private boolean questionDisplayed = false;
    private boolean answersDisplayed = false;

    public Long getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(Long idQuestion) {
        this.idQuestion = idQuestion;
    }

    public Long getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(Long idRoom) {
        this.idRoom = idRoom;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isQuestionDisplayed() {
        return questionDisplayed;
    }

    public void setQuestionDisplayed(boolean questionDisplayed) {
        this.questionDisplayed = questionDisplayed;
    }

    public boolean isAnswersDisplayed() {
        return answersDisplayed;
    }

    public void setAnswersDisplayed(boolean answersDisplayed) {
        this.answersDisplayed = answersDisplayed;
    }
}
