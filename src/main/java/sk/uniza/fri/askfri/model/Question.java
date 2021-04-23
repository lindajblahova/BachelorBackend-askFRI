package sk.uniza.fri.askfri.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/** Trieda mapovana na tabulku question databazy, sluzi pre zaznamenanie otazok
 *  pre miestnost
 *  obsahuje ID otazky, rodicovsku miestnost a obsah otazky
 *  ID je primarnym klucom, ktory je generovany sekvenciou q_id_seq
 *  Question je vo vztahu ManyToOne k rodicovskej miestnosti, referencuje ID miestnosti
 *  Question je vo vztahu OneToMany k OptionalAnswer ako rodicovska otazka
 *  Question je vo vztahu OneToMany k Answer ako rodicovska otazka
 *  Question je vo vztahu OneToMany k AnsweredQuestion ako rodicovska otazka
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@Entity(name = "question")
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "question_generator")
    @SequenceGenerator(name = "question_generator", sequenceName = "q_id_seq", allocationSize = 1)
    @Column(nullable = false, updatable = false)
    private Long idQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_id_room", referencedColumnName = "idRoom", nullable=false)
    private Room idRoom;

    @Column(
            name = "type",
            updatable = false,
            nullable = false,
            columnDefinition = "INT"
    )
    private Integer type;

    @Column(
            name = "content",
            updatable = false,
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String content;

    @Column(
            name = "question_displayed",
            nullable = false,
            columnDefinition = "BOOLEAN"
    )
    private boolean questionDisplayed;

    @Column(
            name = "answers_displayed",
            nullable = false,
            columnDefinition = "BOOLEAN"
    )
    private boolean answersDisplayed;

    @OneToMany(mappedBy = "idQuestion",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final Set<Answer> answersSet = new HashSet<Answer>();

    @OneToMany(mappedBy = "idQuestion",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final Set<AnsweredQuestion> answeredQuestionSet = new HashSet<AnsweredQuestion>();

    @OneToMany(mappedBy = "idQuestion",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final Set<OptionalAnswer> optionalAnswerSet = new HashSet<OptionalAnswer>();

    public Question(Room idRoom, Integer type, String content, boolean questionDisplayed, boolean answersDisplayed) {
        this.idRoom = idRoom;
        this.type = type;
        this.content = content;
        this.questionDisplayed = questionDisplayed;
        this.answersDisplayed = answersDisplayed;
    }

    public Question() {}

    public Long getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(Long idQuestion) {
        this.idQuestion = idQuestion;
    }

    public Long getIdRoom() {
        return idRoom.getIdRoom();
    }

    public void setIdRoom(Room idRoom) {
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

    public Set<OptionalAnswer> getOptionalAnswerSet() {
        return optionalAnswerSet;
    }

    public void addOptionalAnswer(OptionalAnswer optionalAnswer)
    {
        if (!this.optionalAnswerSet.contains(optionalAnswer))
        {
            this.optionalAnswerSet.add(optionalAnswer);
            optionalAnswer.setIdQuestion(this);
        }
    }

    public void removeOptionalAnswer(OptionalAnswer optionalAnswer)
    {
        if (this.optionalAnswerSet.contains(optionalAnswer))
        {
            this.optionalAnswerSet.remove(optionalAnswer);
            optionalAnswer.setIdQuestion(null);
        }
    }

    public Set<AnsweredQuestion> getAnsweredQuestionSet() {
        return answeredQuestionSet;
    }

    public void addAnsweredQuestion(AnsweredQuestion answeredQuestion)
    {
        if (!this.answeredQuestionSet.contains(answeredQuestion))
        {
            this.answeredQuestionSet.add(answeredQuestion);
            answeredQuestion.setIdQuestion(this);
        }
    }

    public void removeAnsweredQuestion(AnsweredQuestion answeredQuestion)
    {
        if (this.answeredQuestionSet.contains(answeredQuestion))
        {
            this.answeredQuestionSet.remove(answeredQuestion);
            answeredQuestion.setIdQuestion(null);
        }
    }

    public Set<Answer> getAnswersSet() {
        return answersSet;
    }

    public void addAnswer(Answer answer)
    {
        if (!this.answersSet.contains(answer))
        {
            this.answersSet.add(answer);
            answer.setIdQuestion(this);
        }
    }

    public void removeAnswer(Answer answer)
    {
        if (this.answersSet.contains(answer))
        {
            this.answersSet.remove(answer);
            answer.setIdQuestion(null);
        }
    }
}
