package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.User;

import java.util.Set;

@Repository
public interface IRoomRepository extends JpaRepository<Room, Long> {
    boolean existsByRoomPasscodeAndActive(String roomPasscode, boolean isActive);

    Room findByRoomPasscodeAndActive(String roomPasscode, boolean isActive);

    Room findRoomByIdRoom(Long idRoom);

    Set<Room> findAllByOrderByIdRoomAsc();

   /* @EntityGraph(value = "userRooms", type = EntityGraph.EntityGraphType.LOAD)
    Set<Room> findByIdOwner(User user);*/

    @Modifying
    @Query("DELETE FROM room u WHERE u.idOwner.idUser=?1")
    void deleteRoomsByIdOwner_IdUser(User user);

    @Modifying
    @Query("DELETE FROM room u WHERE u.idOwner.idUser=?1")
    void deleteAllByIdOwner_IdUser(Long idUser);
}

