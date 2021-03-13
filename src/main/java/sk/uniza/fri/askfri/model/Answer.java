package sk.uniza.fri.askfri.model;

import javax.persistence.*;

@Entity(name = "answer")
@Table(name = "answer")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idAnswer;

    @ManyToOne
    @JoinColumn(name="id_question", nullable=false)
    private Question idQuestion;

    @Column(
            name = "content",
            updatable = false,
            columnDefinition = "TEXT"
    )
    private String content;

    public Answer(Question idQuestion, String content) {
        this.idQuestion = idQuestion;
        this.content = content;
    }

    public Answer() {}

    public Long getIdAnswer() {
        return idAnswer;
    }

    public void setIdAnswer(Long idAnswer) {
        this.idAnswer = idAnswer;
    }

    public Long getIdQuestion() {
        return idQuestion.getIdQuestion();
    }

    public void setIdQuestion(Question idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
