package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.User;

import java.util.List;

@Repository
public interface IRoomRepository extends JpaRepository<Room, Long> {
    boolean existsByRoomPasscodeAndActive(String roomPasscode, boolean isActive);
    Room findByRoomPasscodeAndActive(String roomPasscode, boolean isActive);
    Room findRoomByIdRoom(Long idRoom);
    List<Room> findAllByOrderByIdRoomAsc();
    List<Room> findByIdOwnerOrderByIdRoomDesc(User owner);
    void deleteAllByIdOwner(User owner);
}
