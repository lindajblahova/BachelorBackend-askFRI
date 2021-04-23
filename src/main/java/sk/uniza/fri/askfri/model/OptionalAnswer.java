package sk.uniza.fri.askfri.model;

import javax.persistence.*;

/** Trieda mapovana na tabulku optional_answer databazy, sluzi pre zaznamenanie moznosti
 *  odpovede pre otazku
 *  obsahuje ID moznosti, rodicovsku otazku a obsah moznosti
 *  ID je primarnym klucom, ktory je generovany sekvenciou oa_id_seq
 *  OptionalAnswer je vo vztahu ManyToOne k rodicovskej otazke, referencuje ID otazky
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@Entity(name = "optionalAnswer")
@Table(name = "optional_answer")
public class OptionalAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "opt_ans_generator")
    @SequenceGenerator(name = "opt_ans_generator", sequenceName = "oa_id_seq", allocationSize = 1)
    @Column(nullable = false, updatable = false)
    private Long idOptionalAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="question_id_question", referencedColumnName = "idQuestion", nullable=false)
    private Question idQuestion;

    @Column(
            name = "content",
            updatable = false,
            nullable = false,
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
