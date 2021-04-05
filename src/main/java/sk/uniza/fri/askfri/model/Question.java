package sk.uniza.fri.askfri.model;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "question")
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_room", nullable=false)
    private Room idRoom;

    @Column(
            name = "type",
            updatable = false,
            columnDefinition = "INT"
    )
    private Integer type;

    @Column(
            name = "content",
            updatable = false,
            columnDefinition = "TEXT"
    )
    private String content;

    @Column(
            name = "question_displayed",
            columnDefinition = "BOOLEAN"
    )
    private boolean questionDisplayed;

    @Column(
            name = "answers_displayed",
            columnDefinition = "BOOLEAN"
    )
    private boolean answersDisplayed;

    @OneToMany(mappedBy = "idQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Answer> questionSet = new HashSet<Answer>();

    @OneToMany(mappedBy = "idQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<AnsweredQuestion> answeredQuestionSet = new HashSet<AnsweredQuestion>();

    @OneToMany(mappedBy = "idQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
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

    public Set<AnsweredQuestion> getAnsweredQuestionSet() {
        return answeredQuestionSet;
    }
}
