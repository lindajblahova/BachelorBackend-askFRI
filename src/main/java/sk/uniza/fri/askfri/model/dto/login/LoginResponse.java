package sk.uniza.fri.askfri.model.dto.login;

public class LoginResponse {

    private String token;
    private long id;
    private String role;

    public LoginResponse(String token, long id, String role) {
        this.token = token;
        this.id = id;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
