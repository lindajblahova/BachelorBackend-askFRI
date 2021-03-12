package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.User;

import java.util.List;

public interface IRoomRepository extends JpaRepository<Room, Long> {
    boolean existsByRoomPasscode(String roomPasscode);
    Room findByRoomPasscode(String roomPasscode);
    List<Room> findAllByOrderByIdRoomAsc();
    List<Room> findByOwnerOrderByIdRoomDesc(User owner);
}
