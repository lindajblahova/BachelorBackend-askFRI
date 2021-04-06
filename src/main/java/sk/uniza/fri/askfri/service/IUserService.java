package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.AnsweredQuestion;
import sk.uniza.fri.askfri.model.LikedMessage;
import sk.uniza.fri.askfri.model.User;

import java.util.List;

public interface IUserService {

    User saveUser(User user);
    boolean existsByEmail(String email);
    User getUserByEmail(String email); //throws NotFoundException;
    User getUserByIdUser(Long idUser);
    List<User> getAllUsers();
    void deleteUser(long id);

    AnsweredQuestion saveAnsweredQuestion(AnsweredQuestion answeredQuestion);

    LikedMessage saveMessageLike(LikedMessage likedMessage);
    void deleteMessageLike(Long idLikedMessage);
}
