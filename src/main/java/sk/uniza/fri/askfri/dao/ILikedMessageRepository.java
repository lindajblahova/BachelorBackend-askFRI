package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.askfri.model.LikedMessage;

import java.util.List;

@Repository
public interface ILikedMessageRepository extends JpaRepository<LikedMessage, Long> {

    Integer countAllByIdMessage_IdMessage(Long idMessage) ;

    //@Query(value = "SELECT idMessage.idMessage FROM LikedMessage WHERE idUser.idUser=?1")
    List<Integer> findAllByIdUser_IdUser(Long idUser);
}
