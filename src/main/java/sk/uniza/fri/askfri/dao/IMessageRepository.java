package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.askfri.model.Message;
import sk.uniza.fri.askfri.model.Room;

import java.util.List;

@Repository
public interface IMessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByIdRoom(Room room);
    Message findByIdMessage(Long idMessage);
}
