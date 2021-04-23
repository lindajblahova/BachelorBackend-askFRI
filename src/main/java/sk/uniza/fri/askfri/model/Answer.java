package sk.uniza.fri.askfri.model;

import javax.persistence.*;

/** Trieda mapovana na tabulku answer databazy, sluzi pre odpoved na otazku
 *  obsahuje ID odpovede, rodicovsku otazky a obsah odpovede
 *  ID je primarnym klucom, ktory je generovany sekvenciou a_id_seq
 *  Odpoved je vo vztahu ManyToOne k rodicovskej otazke, referencuje ID otazky
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@Entity(name = "answer")
@Table(name = "answer")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "answer_generator")
    @SequenceGenerator(name = "answer_generator", sequenceName = "a_id_seq", allocationSize = 1)
    @Column(nullable = false, updatable = false)
    private Long idAnswer;

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
