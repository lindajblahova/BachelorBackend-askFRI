package sk.uniza.fri.askfri.model.dto;

import java.util.Set;

/** Trieda DTO udajov pre spravu s jej setom reakcii
 * obsahuje DTO spravy, Set reakcii na spravu
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
public class MessageWithLikes {
    private MessageDto message;
    private Set<LikedMessageDto> setLikes;

    public MessageWithLikes() {
    }

    public MessageWithLikes(MessageDto message, Set<LikedMessageDto> setLikes) {
        this.message = message;
        this.setLikes = setLikes;
    }

    public MessageDto getMessage() {
        return message;
    }

    public void setMessage(MessageDto message) {
        this.message = message;
    }

    public Set<LikedMessageDto> getSetLikes() {
        return setLikes;
    }

    public void setSetLikes(Set<LikedMessageDto> setLikes) {
        this.setLikes = setLikes;
    }
}
