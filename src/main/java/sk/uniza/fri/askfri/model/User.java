package sk.uniza.fri.askfri.model;

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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
            columnDefinition = "VARCHAR(80)"

    )
    private String password;
    @Column(
            name = "role",
            columnDefinition = "VARCHAR(10)"

    )
    private String role;

    @OneToMany(mappedBy = "owner")
    private Set<Room> roomSet = new HashSet<Room>();

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
}
