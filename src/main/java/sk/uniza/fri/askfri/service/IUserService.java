package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.AnsweredQuestion;
import sk.uniza.fri.askfri.model.LikedMessage;
import sk.uniza.fri.askfri.model.LikedMessageId;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.AnsweredQuestionDto;

import java.util.List;
import java.util.Set;

public interface IUserService {

    User saveUser(User user);
    boolean existsByEmail(String email);
    User getUserByEmail(String email); //throws NotFoundException;
    User getUserByIdUser(Long idUser);
    Set<User> getAllUsers();
    void deleteUser(long id);

    AnsweredQuestion saveAnsweredQuestion(AnsweredQuestion answeredQuestion);

    LikedMessage saveLikedMessage(LikedMessage likedMessage);
    void deleteLikedMessage(LikedMessageId likedMessage);

}
