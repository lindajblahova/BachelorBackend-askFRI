package sk.uniza.fri.askfri.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/** Trieda mapovana na tabulku user_profile databazy, sluzi pre zaznamenanie pouzivatelov
 *  obsahuje ID pouzivatela, krstne meno a priezvisko pouzivatela, unikatny email
 *  pouzivatela, heslo pouzivatela a jeho rolu
 *  ID je primarnym klucom, ktory je generovany sekvenciou u_id_seq
 *  User je vo vztahu OneToMany k Room ako rodicovsky vlastnik miestnosti
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@Entity(name = "user_profile")
@Table(
        name = "user_profile",
        uniqueConstraints = {
                @UniqueConstraint(name = "user_email_unique", columnNames = "email")
        }
        )
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name = "user_generator", sequenceName = "u_id_seq", allocationSize = 1)
    @Column(nullable = false, updatable = false)
    private Long idUser;

    @Column(
            name = "firstname",
            updatable = false,
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String firstname;
    @Column(
            name = "surname",
            updatable = false,
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String surname;
    @Column(
            name = "email",
            updatable = false,
            nullable = false,
            columnDefinition = "TEXT"

    )
    private String email;
    @Column(
            name = "password",
            nullable = false,
            columnDefinition = "VARCHAR(60)"

    )
    private String password;
    @Column(
            name = "role",
            nullable = false,
            updatable = false,
            columnDefinition = "VARCHAR(10)"

    )
    private String role;

    @OneToMany(mappedBy = "idOwner",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final Set<Room> roomSet = new HashSet<Room>();

    @OneToMany(mappedBy = "idUser",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY , orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final Set<AnsweredQuestion> answeredQuestionSet = new HashSet<AnsweredQuestion>();

    @OneToMany(mappedBy = "idUser",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY , orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final Set<LikedMessage> likedMessageSet = new HashSet<LikedMessage>();

    public User(String firstname,
                String surname, String email,
                String password, String role) {
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User() {}

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<AnsweredQuestion> getAnsweredQuestionSet() {
        return answeredQuestionSet;
    }

    public void addAnsweredQuestion(AnsweredQuestion answeredQuestion)
    {
        if (!this.answeredQuestionSet.contains(answeredQuestion))
        {
            this.answeredQuestionSet.add(answeredQuestion);
            answeredQuestion.setIdUser(this);
        }
    }

    public void removeAnsweredQuestion(AnsweredQuestion answeredQuestion)
    {
        if (this.answeredQuestionSet.contains(answeredQuestion))
        {
            this.answeredQuestionSet.remove(answeredQuestion);
            answeredQuestion.setIdUser(null);
        }
    }

    public Set<Room> getRoomSet() {
        return roomSet;
    }

    public void addRoom(Room room)
    {
        if (!this.roomSet.contains(room))
        {
            this.roomSet.add(room);
            room.setIdOwner(this);
        }
    }

    public void removeRoom(Room room)
    {
        if (this.roomSet.contains(room))
        {
            this.roomSet.remove(room);
            room.setIdOwner(null);
        }
    }

    public Set<LikedMessage> getLikedMessageSet() {
        return likedMessageSet;
    }

    public void addLikedMessage(LikedMessage likedMessage)
    {
        if (!this.likedMessageSet.contains(likedMessage))
        {
            this.likedMessageSet.add(likedMessage);
            likedMessage.setIdUser(this);
        }
    }

    public void removeLikedMessage(LikedMessage likedMessage)
    {
        if (this.likedMessageSet.contains(likedMessage))
        {
            this.likedMessageSet.remove(likedMessage);
            likedMessage.setIdUser(null);
        }
    }
}
