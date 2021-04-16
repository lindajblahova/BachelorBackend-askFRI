package sk.uniza.fri.askfri.model.dto;

public class LikeMesClass {

    private Long idUser;
    private Long idMessage;

    public LikeMesClass(Long idUser, Long idQuestion) {
        this.idUser = idUser;
        this.idMessage = idQuestion;
    }

    public LikeMesClass() {
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(Long idMessage) {
        this.idMessage = idMessage;
    }
}
