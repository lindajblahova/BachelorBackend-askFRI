package sk.uniza.fri.askfri.model;

import javax.persistence.*;

@Entity(name = "optionalAnswer")
@Table(name = "optional_answer")
public class OptionalAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "opt_ans_generator")
    @SequenceGenerator(name = "opt_ans_generator", sequenceName = "oa_id_seq", allocationSize = 10)
    private Long idOptionalAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="question_id_question", referencedColumnName = "idQuestion", nullable=false, updatable = false)
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
