package sk.uniza.fri.askfri.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.IQuestionRepository;
import sk.uniza.fri.askfri.model.OptionalAnswer;
import sk.uniza.fri.askfri.model.Question;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.dto.OptionalAnswerDto;
import sk.uniza.fri.askfri.model.dto.QuestionDto;
import sk.uniza.fri.askfri.model.dto.ResponseDto;
import sk.uniza.fri.askfri.service.IQuestionService;
import sk.uniza.fri.askfri.service.IRoomService;

import java.util.Set;
import java.util.stream.Collectors;

/** Sluzba pracujuca s Question
 * implementuje IMessageService
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@Service
public class QuestionServiceImplement implements IQuestionService {

    private final IQuestionRepository questionRepository;

    private final IRoomService roomService;
    private final ModelMapper modelMapper;

    public QuestionServiceImplement(IQuestionRepository questionRepository, IRoomService roomService, ModelMapper modelMapper) {
        this.questionRepository = questionRepository;
        this.roomService = roomService;
        this.modelMapper = modelMapper;
    }

    /** Pokusi sa najst rodicovsku miestnost a nasledne premapuje DTO na otazku, ktoru
     * ulozi, aby si pamatala svoje ID kvoli moznostiam k otazke. Dalej je otazka pridana
     * k miestnosti a miestnost je ulozena, na zaver premapuje ulozenu otazku na DTO
     * a vrati ju
     * @param questionDto DTO otazka
     * @return QuestionDto vytvorena otazka
     * @throws NullPointerException pokial nebola najdena rodicovska miestnost alebo
     * obsah otazky bol prazdny
     */
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
        throw new NullPointerException("Otázka nebola vytvorená");
    }

    /** Metoda pre ulozenie otazky
     * @param question otazka
     * @return Question ulozena otazka
     */
    @Override
    public Question saveQuestion(Question question) {
        return this.questionRepository.save(question);
    }


    /** Pokusi sa najst otazku a miestnost do ktorej otazka patri, nasledne z miestnosti
     * odoberie otazku a ulozi miestnost
     * @param idQuestion ID otazky
     * @return ResponseDto odpoved o zmazani otazky
     */
    @Override
    public ResponseDto deleteQuestion(Long idQuestion) {
        Question question = this.questionRepository.findQuestionByIdQuestion(idQuestion);
        Room parentRoom = this.roomService.findByIdRoom(question.getIdRoom());
        parentRoom.removeQuestion(question);
        this.roomService.saveRoom(parentRoom);
        return new ResponseDto(idQuestion, "Otázka bola vymazaná");
    }

    /** Metoda pre najdenie otazky podla jej ID
     * @param idQuestion ID otazky
     * @return Question najdena otazka
     */
    @Override
    public Question findByIdQuestion(Long idQuestion) {
        return this.questionRepository.findQuestionByIdQuestion(idQuestion);
    }

    /** Pokusi sa najst rodicovsku miestnost a nasledne ziskat jej set otazok, ktory je
     * premapovany na dto a vrateny
     * @param idRoom ID miestnosti
     * @return Set<QuestionDto> set vsetkych otazok pre danu miestnost
     * @throws NullPointerException pokial nenajde zadanu miestnost
     */
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

    /** Pokusi sa najst rodicovsku miestnost a nasledne ziskat jej set otazok, ktory je
     * vyfilrovany len na zobrazene otazky, premapovany na dto a vrateny
     * @param idRoom ID miestnosti
     * @return Set<QuestionDto> set aktivnych otazok pre danu miestnost
     * @throws NullPointerException pokial nenajde zadanu miestnost
     */
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

    /** Pokusi sa najst otazku a jej rodicovsku miestnost, upravi viditelnost
     * otazky a ulozi miestnost do ktorej otazka patri
     * vrati odpoved o upraveni viditelnosti
     * @param idQuestion ID otazky
     * @return ResponseDto odpoved o zmene viditelnosti otazky
     * @throws NullPointerException ak nebola najdena otazka alebo miestnost
     */
    @Override
    public ResponseDto updateQuestionDisplayed(Long idQuestion) {
        Question foundQuestion = this.questionRepository.findQuestionByIdQuestion(idQuestion);
        Room parentRoom = this.roomService.findByIdRoom(foundQuestion.getIdRoom());
        if (foundQuestion != null && parentRoom != null) {
            foundQuestion.setQuestionDisplayed(!foundQuestion.isQuestionDisplayed());
            this.roomService.saveRoom(parentRoom);
            return new ResponseDto(foundQuestion.getIdQuestion(),
                    foundQuestion.isQuestionDisplayed() ? "Otázka je zobrazená" :
                            "Otázka nie je zobrazená" );
        }
        throw new NullPointerException("Otázka nebola nájdená");
    }

    /** Pokusi sa najst otazku a jej rodicovsku miestnost, upravi viditelnost vysledkov
     * otazky a ulozi miestnost do ktorej otazka patri
     * vrati odpoved o upraveni viditelnosti
     * @param idQuestion ID otazky
     * @return ResponseDto odpoved o zmene viditelnosti vysledkov otazky
     * @throws NullPointerException ak nebola najdena otazka alebo miestnost
     */
    @Override
    public ResponseDto updateAnswersDisplayed(Long idQuestion) {
        Question foundQuestion = this.questionRepository.findQuestionByIdQuestion(idQuestion);
        Room parentRoom = this.roomService.findByIdRoom(foundQuestion.getIdRoom());
        if (foundQuestion != null && parentRoom != null) {
            //parentRoom.removeQuestion(foundQuestion);
            foundQuestion.setAnswersDisplayed(!foundQuestion.isAnswersDisplayed());
            //parentRoom.addQuestion(foundQuestion);
            this.roomService.saveRoom(parentRoom);
            return new ResponseDto(foundQuestion.getIdQuestion(),
                    foundQuestion.isAnswersDisplayed() ? "Výsledky sú zobrazené" :
                            "Výsledky nie sú zobrazené" );
        }
        throw new NullPointerException("Otázka nebola nájdená");
    }

    /** Skontroluje, ci moznosti nis su prazdne, nasledne najde rodicovsku otazku
     *  a postupne k nej prida vsetky odpovede a nakoniec otazku ulozi
     *  vrati oznamenie o ulozeni moznosti
     * @param optionalAnswerDto pole moznosti pre otazku
     * @return ResponseDto odpoved o ulozeni moznosti pre otazku
     * @throws NullPointerException ak nebola najdena otazka
     * @throws IllegalArgumentException ak bola niektora z moznosti prazdna
     */
    @Override
    public ResponseDto addOptionalAnswer(OptionalAnswerDto[] optionalAnswerDto) {
        for (OptionalAnswerDto answerDto : optionalAnswerDto) {
            if (answerDto.getContent().trim().equals(""))
            {
                throw new IllegalArgumentException("Možnosti otázky neboli vytvorené");
            }
        }
        Question parentQuestion = this.questionRepository.findQuestionByIdQuestion(optionalAnswerDto[0].getIdQuestion());
        if (parentQuestion != null) {
            for (OptionalAnswerDto answerDto : optionalAnswerDto) {
                    OptionalAnswer optionalAnswer = this.modelMapper.map(answerDto, OptionalAnswer.class);
                    parentQuestion.addOptionalAnswer(optionalAnswer);
                    this.questionRepository.save(parentQuestion);
            }
            return new ResponseDto(parentQuestion.getIdQuestion(), "Možnosti bolí vytvorené");
        }
        throw new NullPointerException("Otazka nebola najdena");
    }

    /** Pokusi sa ziskat otazku a nasledne set jej odpovedi, ktory premapuje na dto
     *  a vrati
     * @param idQuestion ID otazky
     * @return Set<OptionalAnswerDto> Set DTO moznosti k pozadovanej otazke
     * @throws NullPointerException ak nebola najdena otazka
     */
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

    /** Metoda pre zistenie, ci je pouzivatel autorom miestnosti
     * @param idUser ID pouzivatela
     * @param idQuestion ID otazky, s ktorou chce pouzivatel vykonat akciu
     * @return Integer pocet najdenych riadkov pouzivatela
     */
    @Override
    public Integer isUserRoomOwnerFromQuestion(Long idUser, Long idQuestion) {
        return this.questionRepository.isUserRoomOwner(idUser,idQuestion);
    }
}
