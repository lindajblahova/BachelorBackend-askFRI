package sk.uniza.fri.askfri.model;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;

@Entity(name = "user_profile")
public class User {

    @Id
    private Long idUser;
    private String firstname;
    private String surname;
    private String email;
    private String password;
    private String role;

    public User(Long idUser,String firstname,
                String surname, String email,
                String password, String role) {
        this.idUser = idUser;
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

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
