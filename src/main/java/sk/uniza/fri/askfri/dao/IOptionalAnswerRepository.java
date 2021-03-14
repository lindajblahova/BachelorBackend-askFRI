package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.uniza.fri.askfri.model.OptionalAnswer;

public interface IOptionalAnswerRepository extends JpaRepository<OptionalAnswer, Long> {

}
