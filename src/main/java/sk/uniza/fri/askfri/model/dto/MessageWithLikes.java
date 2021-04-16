package sk.uniza.fri.askfri.model.dto;

import java.util.Set;

public class MessageWithLikes {
    private MessageDto message;
    private Set<LikeMesClass> setLikes;

    public MessageWithLikes() {
    }

    public MessageWithLikes(MessageDto message, Set<LikeMesClass> setLikes) {
        this.message = message;
        this.setLikes = setLikes;
    }

    public MessageDto getMessage() {
        return message;
    }

    public void setMessage(MessageDto message) {
        this.message = message;
    }

    public Set<LikeMesClass> getSetLikes() {
        return setLikes;
    }

    public void setSetLikes(Set<LikeMesClass> setLikes) {
        this.setLikes = setLikes;
    }
}
