package sk.uniza.fri.askfri.model;

import org.springframework.data.jpa.repository.Query;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "user_profile")
@Table(
        name = "user_profile",
        uniqueConstraints = {
                @UniqueConstraint(name = "user_email_unique", columnNames = "email")
        }
        )
@NamedEntityGraph(name = "userRooms",
        attributeNodes = @NamedAttributeNode("roomSet"))
@NamedEntityGraph(name = "userAnsweredQuestions",
        attributeNodes = @NamedAttributeNode("answeredQuestionSet"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name = "user_generator", sequenceName = "u_id_seq", allocationSize = 10)
    private Long idUser;

    @Column(
            name = "firstname",
            updatable = false,
            columnDefinition = "TEXT"
    )
    private String firstname;
    @Column(
            name = "surname",
            updatable = false,
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
            updatable = false,
            columnDefinition = "VARCHAR(10)"

    )
    private String role;

    @OneToMany(mappedBy = "idOwner", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private final Set<Room> roomSet = new HashSet<Room>();

    @OneToMany(mappedBy = "idUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY , orphanRemoval = true)
    private final Set<AnsweredQuestion> answeredQuestionSet = new HashSet<AnsweredQuestion>();

    @OneToMany(mappedBy = "idUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY , orphanRemoval = true)
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

    public Set<Room> getRoomSet() {
        return roomSet;
    }

    public Set<LikedMessage> getLikedMessageSet() {
        return likedMessageSet;
    }
}
