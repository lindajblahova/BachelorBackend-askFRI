package sk.uniza.fri.askfri.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.IAnsweredQuestionRepository;
import sk.uniza.fri.askfri.model.AnsweredQuestion;
import sk.uniza.fri.askfri.model.Question;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.AnsweredQuestionDto;
import sk.uniza.fri.askfri.model.dto.ResponseDto;
import sk.uniza.fri.askfri.service.IAnsweredQuestionService;
import sk.uniza.fri.askfri.service.IQuestionService;
import sk.uniza.fri.askfri.service.IUserService;

import java.util.Set;
import java.util.stream.Collectors;

/** Sluzba pracujuca s AnsweredQuestion
 * implementuje IAnsweredQuestionService
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@Service
public class AnsweredQuestionServiceImplement implements IAnsweredQuestionService {

    private final IUserService userService;
    private final IQuestionService questionService;
    private final IAnsweredQuestionRepository answeredQuestionRepository;
    private final ModelMapper modelMapper;

    public AnsweredQuestionServiceImplement(IUserService userService, IQuestionService questionService, IAnsweredQuestionRepository answeredQuestionRepository, ModelMapper modelMapper) {
        this.userService = userService;
        this.questionService = questionService;
        this.answeredQuestionRepository = answeredQuestionRepository;
        this.modelMapper = modelMapper;
    }

    /** Metoda kontroluje existenciu zadaneho pouivatela a otazky,
     *  pokial kontrola prebehne v poriadku, vytvore novy zaznam pouzivatelom
     *  zodpovedanej otazky a prida ho k pouzivatelovi aj k otazke
     * @param answeredQuestionDto pouzivatelom zodpovedana otazka DTO
     * @return ResponseDto odpoved o ulozeni zaznamu zodpovedanej otazky
     * @throws NullPointerException pokial nebol najdeny pouzivatel alebo otazka
     */
    @Override
    public ResponseDto saveAnsweredQuestion(AnsweredQuestionDto answeredQuestionDto) {
        Question parentQuestion = this.questionService.findByIdQuestion(answeredQuestionDto.getIdQuestion());
        User parentUser = this.userService.findUserByIdUser(answeredQuestionDto.getIdUser());

        if (parentQuestion != null && parentUser != null ) {
            AnsweredQuestion answeredQuestion = new AnsweredQuestion(parentUser,parentQuestion);
            answeredQuestion = this.answeredQuestionRepository.save(answeredQuestion);
            parentUser.addAnsweredQuestion(answeredQuestion);
            parentQuestion.addAnsweredQuestion(answeredQuestion);
            this.userService.saveUser(parentUser);
            this.questionService.saveQuestion(parentQuestion);
            return new ResponseDto(answeredQuestion.hashCode(),"Odpoveď používateľa bola zaznamená");
        }
        throw new  NullPointerException("Pouzivatel alebo otazka neexistuje");

    }

    /** Pokusi sa najst pozadovaneho pouzivatela a ak existuje, tak prenho vrati set
     *  vsetkych nim zodpovedanych otazok, ktory je nasledne filtrovany aby obsahoval
     *  len zodpovedane otazky pre pozadovanu miestnost
     * @param idUser ID pouzivatela
     * @param idRoom ID miestnosti
     * @return Set<AnsweredQuestionDto> set pouzivatelom zodpovedanych otazok
     * @throws NullPointerException pokial nebol najdeny pouzivatel
     */
    @Override
    public Set<AnsweredQuestionDto> getAllUserQuestionAnswers(Long idUser, Long idRoom) {
        User parentUser = this.userService.findUserByIdUser(idUser);
        Set<AnsweredQuestion> answerSet = parentUser.getAnsweredQuestionSet()
                .stream()
                .filter(answeredQuestion -> answeredQuestion.getRoomFromQuestion().equals(idRoom)).collect(Collectors.toSet());

        if (parentUser != null) {
            return answerSet
                    .stream()
                    .map(answeredQuestion -> this.modelMapper
                            .map(answeredQuestion, AnsweredQuestionDto.class))
                    .collect(Collectors.toSet());
        }
        throw new NullPointerException("Používateľ nebol nájdený");
    }
}
