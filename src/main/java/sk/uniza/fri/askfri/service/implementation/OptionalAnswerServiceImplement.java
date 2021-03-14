package sk.uniza.fri.askfri.service.implementation;

import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.IOptionalAnswerRepository;
import sk.uniza.fri.askfri.model.OptionalAnswer;
import sk.uniza.fri.askfri.service.IOptionalAnswerService;

@Service
public class OptionalAnswerServiceImplement implements IOptionalAnswerService {

    private final IOptionalAnswerRepository optionalAnswerRepository;

    public OptionalAnswerServiceImplement(IOptionalAnswerRepository optionalAnswerRepository) {
        this.optionalAnswerRepository = optionalAnswerRepository;
    }

    @Override
    public OptionalAnswer saveOptionalAnswer(OptionalAnswer optionalAnswer) {
        return this.optionalAnswerRepository.save(optionalAnswer);
    }
}
