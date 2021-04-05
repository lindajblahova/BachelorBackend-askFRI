package sk.uniza.fri.askfri.service.implementation;


import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.IAnsweredQuestionRepository;
import sk.uniza.fri.askfri.dao.ILikedMessageRepository;
import sk.uniza.fri.askfri.dao.IUserRepository;
import sk.uniza.fri.askfri.model.AnsweredQuestion;
import sk.uniza.fri.askfri.model.LikedMessage;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.service.IUserService;

import java.util.List;

@Service
public class UserServiceImplement implements IUserService {

    private final IUserRepository userRepository;
    private final IAnsweredQuestionRepository answeredQuestionRepository;
    private final ILikedMessageRepository likedMessageRepository;


    public UserServiceImplement(IUserRepository userRepository, IAnsweredQuestionRepository answeredQuestionRepository, ILikedMessageRepository likedMessageRepository) {
        this.userRepository = userRepository;
        this.answeredQuestionRepository = answeredQuestionRepository;
        this.likedMessageRepository = likedMessageRepository;
    }


    @Override
    public User saveUser(User user) {
        this.userRepository.save(user);
        return user;
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public User getUserByEmail(String email) {
        User existingUser = this.userRepository.findByEmail(email);
        if (existingUser != null) {
            return existingUser;
        } else {
            //TODO: return exception
            return existingUser;
        }
    }

    @Override
    public User getUserByIdUser(Long idUser) {
        return this.userRepository.findByIdUser(idUser);
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAllByOrderBySurnameAsc();
    }

    @Override
    public void deleteUser(long id) {
            this.userRepository.deleteById(id);
    }

    @Override
    public AnsweredQuestion saveAnsweredQuestion(AnsweredQuestion answeredQuestion) {
        return this.answeredQuestionRepository.save(answeredQuestion);
    }

    @Override
    public LikedMessage saveMessageLike(LikedMessage likedMessage) {
        return this.likedMessageRepository.save(likedMessage);
    }

    @Override
    public void deleteMessage(Long idLikedMessage) {
        this.likedMessageRepository.deleteById(idLikedMessage);
    }

}
