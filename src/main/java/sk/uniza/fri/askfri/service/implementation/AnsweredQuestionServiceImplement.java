package sk.uniza.fri.askfri.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.IAnsweredQuestionRepository;
import sk.uniza.fri.askfri.model.AnsweredQuestion;
import sk.uniza.fri.askfri.model.Question;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.AnsQuestionClass;
import sk.uniza.fri.askfri.model.dto.AnsweredQuestionDto;
import sk.uniza.fri.askfri.model.dto.ResponseDto;
import sk.uniza.fri.askfri.service.IAnsweredQuestionService;
import sk.uniza.fri.askfri.service.IQuestionService;
import sk.uniza.fri.askfri.service.IUserService;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
        throw new  NullPointerException("Otázka nebola nájdená");

    }

    @Override
    public Set<AnsweredQuestionDto> getAllUserQuestionAnswers(Long idUser) {
        User parentUser = this.userService.findUserByIdUser(idUser);
        Set<AnsweredQuestion> answerSet = parentUser.getAnsweredQuestionSet();
        Set<AnsQuestionClass> dtoSet = new HashSet<AnsQuestionClass>();
        if (parentUser != null) {
            answerSet.forEach(data -> dtoSet.add(new AnsQuestionClass(data.getIdUser().getIdUser(),data.getIdQuestion().getIdQuestion())));
            return dtoSet.stream().map(data -> this.modelMapper.map(data,
                    AnsweredQuestionDto.class)).collect(Collectors.toSet());
        }
        throw new NullPointerException("Používateľ nebol nájdený");
    }
}
