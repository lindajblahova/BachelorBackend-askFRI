package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.uniza.fri.askfri.model.Message;
import sk.uniza.fri.askfri.model.Room;

import java.util.List;

public interface IMessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByIdRoom(Room room);

}
