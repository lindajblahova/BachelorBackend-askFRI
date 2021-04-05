package sk.uniza.fri.askfri.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.uniza.fri.askfri.model.LikedMessage;

@Repository
public interface ILikedMessageRepository extends JpaRepository<LikedMessage, Long> {

}
