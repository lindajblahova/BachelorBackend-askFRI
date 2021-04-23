package sk.uniza.fri.askfri.model.dto.login;

/** Trieda DTO udajov pre login pouzivatela
 * obsahuje email a heslo
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
public class LoginForm {

    private String email;
    private String password;

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
}
