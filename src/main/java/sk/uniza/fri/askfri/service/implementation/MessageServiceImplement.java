package sk.uniza.fri.askfri.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.IMessageRepository;
import sk.uniza.fri.askfri.model.LikedMessage;
import sk.uniza.fri.askfri.model.Message;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.dto.*;
import sk.uniza.fri.askfri.service.IMessageService;
import sk.uniza.fri.askfri.service.IRoomService;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/** Sluzba pracujuca s Message
 * implementuje IMessageService
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@Service
public class MessageServiceImplement implements IMessageService {

    private final IMessageRepository messageRepository;
    private final IRoomService roomService;
    private final ModelMapper modelMapper;

    public MessageServiceImplement(IMessageRepository messageRepository, IRoomService roomService, ModelMapper modelMapper) {
        this.messageRepository = messageRepository;
        this.roomService = roomService;
        this.modelMapper = modelMapper;
    }

    /** Pokusi sa najst rodicovsku miestnost, premapuje dto na spravu,
     *  prida ju k miestnosti a ulozi miestnost
     * @param messageDto DTO sprava
     * @return ResponseDto odpoved o ulozeni spravy
     * @throws NullPointerException pokial nebola najdena miestnost alebo bola prazdna
     * sprava
     */
    @Override
    public ResponseDto createMessage(MessageDto messageDto) {
        Room parentRoom = this.roomService.findByIdRoom(messageDto.getIdRoom());
        Message message = modelMapper.map(messageDto, Message.class);
        if (parentRoom != null && !messageDto.getContent().trim().equals("")) {
            parentRoom.addMessage(message);
            this.roomService.saveRoom(parentRoom);
            return new ResponseDto(message.getIdMessage(), "Správa bola vytvorená");
        }
        throw new NullPointerException("Správu sa nepodarilo vytovriť");
    }

    /** Metoda pre ulozenie spravy
     * @param message sprava
     * @return Message ulozena sprava
     */
    @Override
    public Message saveMessage(Message message) {
        return this.messageRepository.save(message);
    }


    /** Pokusi sa najst rodicovsku miestnost a nasledne najde set sprav ktore k nej
     *  patria. Tento set prejde a pre kazdu spravu najde set reakcii, ktory k nej patri
     *  nasledne premapuje spravu na dto a vrati set sprav spolu so setmi ich reakcii
     * @param idRoom ID miestnosti
     * @return Set<MessageWithLikes> set odpovedi s reakciami pre danu miestnost
     * @throws NullPointerException pokial nie je najdena miestnost
     */
    @Override
    public Set<MessageWithLikes> getAllRoomMessages(Long idRoom) {
        Room parentRoom = this.roomService.findByIdRoom(idRoom);
        if (parentRoom != null) {
            Set<MessageWithLikes> setMessageWithLikes = new HashSet<MessageWithLikes>();
            Set<Message> setMessages = parentRoom.getMessagesSet();
            setMessages.forEach(message ->
            {
                Set<LikedMessage> likedMessageSet = message.getLikedMessageSet();
                if (likedMessageSet != null) {
                    Set<LikedMessageDto> dtoSet = likedMessageSet.stream()
                            .map(likedMessage -> this.modelMapper.map(likedMessage, LikedMessageDto.class))
                            .collect(Collectors.toSet());
                    setMessageWithLikes.add(new MessageWithLikes(
                            this.modelMapper.map(message, MessageDto.class), dtoSet));
                }
            });
            return setMessageWithLikes;
        }
        throw new NullPointerException("Správy miestnosti neboli nájdené");
    }

    /** Pokusi sa najst spravu a jej rodicovsku miestnost a nasledne zmaze odstrani
     * spravu z miestnosti a ulozi miestnost
     * @param idMessage ID spravy
     * @return ResponseDto odpoved o zmazani spravy
     * @throws NullPointerException ak nebola najdena miestnost alebo sprava
     */
    @Override
    public ResponseDto deleteMessage(Long idMessage) {
        Message message = this.messageRepository.findByIdMessage(idMessage);
        Room parentRoom = this.roomService.findByIdRoom(message.getIdRoom());
        if (parentRoom != null && message != null) {
            parentRoom.removeMessage(message);
            this.roomService.saveRoom(parentRoom);
            return new ResponseDto(idMessage, "Správa bola vymazaná");
        }
        throw new NullPointerException("Sprava alebo miestnost nebola najdena");
    }


    /** Najde spravu podla jej ID a vrati ju
     * @param idMessage ID spravy
     * @return  Message najdena sprava
     */
    @Override
    public Message findByIdMessage(Long idMessage) {
        return this.messageRepository.findByIdMessage(idMessage);
    }

    /** Metoda pre zistenie, ci je pouzivatel autorom miestnosti
     * @param idUser ID pouzivatela
     * @param idMessage ID spravy, s ktorou chce pouzivatel vykonat akciu
     * @return Integer pocet najdenych riadkov pouzivatela
     */
    @Override
    public Integer isUserRoomOwnerFromMessage(Long idUser, Long idMessage) {
        return this.messageRepository.isUserRoomOwner(idUser,idMessage);
    }

}
