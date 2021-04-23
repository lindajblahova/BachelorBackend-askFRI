package sk.uniza.fri.askfri.model.dto;

/** Trieda DTO udajov pre odpoved, kde nie je potrebne posielat nazad DTO so vsetkymi udajmi
 * obsahuje ID objektu a spravu o vysledku akcie
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
public class ResponseDto {
    private long id;
    private String message;

    public ResponseDto(long id, String message) {
        this.id = id;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
