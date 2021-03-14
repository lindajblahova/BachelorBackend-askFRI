package sk.uniza.fri.askfri.model;

import javax.persistence.*;

@Entity(name = "optionalAnswer")
@Table(name = "optional_answer")
public class OptionalAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idOptionalAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_question", nullable=false)
    private Question idQuestion;

    @Column(
            name = "content",
            updatable = false,
            columnDefinition = "TEXT"
    )
    private String content;


    public OptionalAnswer(Question idQuestion, String content) {
        this.idQuestion = idQuestion;
        this.content = content;
    }

    public OptionalAnswer() {}

    public Long getIdOptionalAnswer() {
        return idOptionalAnswer;
    }

    public void setIdOptionalAnswer(Long idOptionalAnswer) {
        this.idOptionalAnswer = idOptionalAnswer;
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
