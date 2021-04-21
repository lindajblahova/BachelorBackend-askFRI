package sk.uniza.fri.askfri.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.IOptionalAnswerRepository;
import sk.uniza.fri.askfri.dao.IQuestionRepository;
import sk.uniza.fri.askfri.model.Message;
import sk.uniza.fri.askfri.model.OptionalAnswer;
import sk.uniza.fri.askfri.model.Question;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.dto.OptionalAnswerDto;
import sk.uniza.fri.askfri.model.dto.QuestionDto;
import sk.uniza.fri.askfri.model.dto.ResponseDto;
import sk.uniza.fri.askfri.service.IAnswerService;
import sk.uniza.fri.askfri.service.IQuestionService;
import sk.uniza.fri.askfri.service.IRoomService;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImplement implements IQuestionService {

    private final IQuestionRepository questionRepository;
    private final IOptionalAnswerRepository optionalAnswerRepository;

    private final IRoomService roomService;
    private final ModelMapper modelMapper;

    public QuestionServiceImplement(IQuestionRepository questionRepository, IOptionalAnswerRepository optionalAnswerRepository, IRoomService roomService, ModelMapper modelMapper) {
        this.questionRepository = questionRepository;
        this.optionalAnswerRepository = optionalAnswerRepository;
        this.roomService = roomService;
        this.modelMapper = modelMapper;
    }

    @Override
    public QuestionDto createQuestion(QuestionDto questionDto) {
        Room parentRoom = this.roomService.findByIdRoom(questionDto.getIdRoom());
        Question question = modelMapper.map(questionDto, Question.class);
        if (parentRoom != null && !questionDto.getContent().trim().equals("") &&
                (questionDto.getType() < 4 && questionDto.getType() > -1) ) {

            question = this.questionRepository.save(question);
            parentRoom.addQuestion(question);
            this.roomService.saveRoom(parentRoom);
            return this.modelMapper.map(question, QuestionDto.class);
        }
        throw new IllegalArgumentException("Otázka nebola vytvorená");
    }

    @Override
    public Question saveQuestion(Question question) {
        return this.questionRepository.save(question);
    }

    @Override
    public Question saveQuestionFlush(Question question) {
        return this.questionRepository.saveAndFlush(question);
    }

    @Override
    public ResponseDto deleteQuestion(Long idQuestion) {
        Question question = this.questionRepository.findQuestionByIdQuestion(idQuestion);
        Room parentRoom = this.roomService.findByIdRoom(question.getIdRoom());
        parentRoom.removeQuestion(question);
        this.questionRepository.deleteById(idQuestion); // TODO Added
        this.roomService.saveRoom(parentRoom);
        return new ResponseDto(idQuestion, "Otázka bola vymazaná");
    }

    @Override
    public Question findByIdQuestion(Long idQuestion) {
        return this.questionRepository.findQuestionByIdQuestion(idQuestion);
    }

    @Override
    public Set<QuestionDto> findQuestionsByRoom(Long idRoom) {
        Room parentRoom = this.roomService.findByIdRoom(idRoom);
        if (parentRoom != null) {
            return parentRoom.getQuestionSet()
                    .stream()
                    .map(question -> modelMapper.map(question, QuestionDto.class))
                    .collect(Collectors.toSet());
        }
        throw new NullPointerException("Miestnosť nebola nájdená");
    }

    @Override
    public Set<QuestionDto> findQuestionsByRoomAndActive(Long idRoom) {
        Room parentRoom = this.roomService.findByIdRoom(idRoom);
        if (parentRoom != null) {
            return parentRoom.getQuestionSet()
                    .stream()
                    .filter(Question::isQuestionDisplayed)
                    .map(question -> modelMapper.map(question, QuestionDto.class))
                    .collect(Collectors.toSet());
        }
        throw new NullPointerException("Miestnosť nebola nájdená");
    }

    @Override
    public ResponseDto updateQuestionDisplayed(Long idQuestion) {
        Question foundQuestion = this.questionRepository.findQuestionByIdQuestion(idQuestion);
        Room parentRoom = this.roomService.findByIdRoom(foundQuestion.getIdRoom());
        if (foundQuestion != null && parentRoom != null) {
            parentRoom.removeQuestion(foundQuestion);
            foundQuestion.setQuestionDisplayed(!foundQuestion.isQuestionDisplayed());
            parentRoom.addQuestion(foundQuestion);
            this.roomService.saveRoom(parentRoom);
            return new ResponseDto(foundQuestion.getIdQuestion(),
                    foundQuestion.isQuestionDisplayed() ? "Otázka je zobrazená" :
                            "Otázka nie je zobrazená" );
        }
        throw new NullPointerException("Otázka nebola nájdená");
    }

    @Override
    public ResponseDto updateAnswersDisplayed(Long idQuestion) {
        Question foundQuestion = this.questionRepository.findQuestionByIdQuestion(idQuestion);
        Room parentRoom = this.roomService.findByIdRoom(foundQuestion.getIdRoom());
        if (foundQuestion != null && parentRoom != null) {
            parentRoom.removeQuestion(foundQuestion);
            foundQuestion.setAnswersDisplayed(!foundQuestion.isAnswersDisplayed());
            parentRoom.addQuestion(foundQuestion);
            this.roomService.saveRoom(parentRoom);
            return new ResponseDto(foundQuestion.getIdQuestion(),
                    foundQuestion.isAnswersDisplayed() ? "Výsledky sú zobrazené" :
                            "Výsledky nie sú zobrazené" );
        }
        throw new NullPointerException("Otázka nebola nájdená");
    }

    @Override
    public ResponseDto addOptionalAnswer(OptionalAnswerDto[] optionalAnswerDto) {
        Question parentQuestion = this.questionRepository.findQuestionByIdQuestion(optionalAnswerDto[0].getIdQuestion());
        OptionalAnswer optionalAnswer;
        if (parentQuestion != null) {
            for (OptionalAnswerDto answerDto : optionalAnswerDto) {
                if (!answerDto.getContent().trim().equals("")) {
                    optionalAnswer = this.modelMapper.map(answerDto, OptionalAnswer.class);
                    parentQuestion.addOptionalAnswer(optionalAnswer);
                }
            }
            this.questionRepository.save(parentQuestion);
            return new ResponseDto(parentQuestion.getIdQuestion(), "Možnosti bolí vytvorené");
        }
        throw new IllegalArgumentException("Možnosti otázky neboli vytvorené");
    }

    @Override
    public Set<OptionalAnswerDto> getQuestionOptionalAnswers(Long idQuestion) {
        Question parentQuestion = this.questionRepository.findQuestionByIdQuestion(idQuestion);
        if (parentQuestion != null) {
            return parentQuestion.getOptionalAnswerSet().stream()
                    .map(optionalAnswer -> modelMapper.map(optionalAnswer, OptionalAnswerDto.class))
                    .collect(Collectors.toSet());
        }
        throw new NullPointerException("Otázka pre možnosti nebola nájdená");
    }

}
