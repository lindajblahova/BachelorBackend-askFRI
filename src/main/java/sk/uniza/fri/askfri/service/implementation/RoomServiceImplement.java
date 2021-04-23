package sk.uniza.fri.askfri.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.IRoomRepository;
import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.ResponseDto;
import sk.uniza.fri.askfri.model.dto.RoomDto;
import sk.uniza.fri.askfri.service.*;

import java.util.Set;
import java.util.stream.Collectors;

/** Sluzba pracujuca s Room
 * implementuje IMessageService
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
@Service
public class RoomServiceImplement implements IRoomService {

    private final IRoomRepository roomRepository;
    private final IUserService userService;
    private final ModelMapper modelMapper;

    public RoomServiceImplement(IRoomRepository roomRepository, IUserService userService, ModelMapper modelMapper) {
        this.roomRepository = roomRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    /** Pokusi sa najst autora miestnosti podla jeho ID a pokial je najdeny a udaje
     *  miestnosti su v poriadku prida miestnost k pouziavtelovi a ulozi pouzivatela
     *  vrati spravu o vytvoreni miestnosti
     * @param roomDto DTO miestnost
     * @return ResponseDto odpoved o vytvoreni miestnosti
     * @throws NullPointerException ak nebol najdeny autor alebu udaje boli zle
     */
    @Override
    public ResponseDto createRoom(RoomDto roomDto) {
        Room newRoom = modelMapper.map(roomDto, Room.class);
        User roomOwner = this.userService.findUserByIdUser(roomDto.getIdOwner());
        if (roomOwner != null && !roomDto.getRoomPasscode().trim().equals("") &&
                !roomDto.getRoomName().trim().equals(""))
        {
            roomOwner.addRoom(newRoom);
            this.userService.saveUser(roomOwner);
            return new ResponseDto(newRoom.getIdRoom(),
                    "Miestnosť " + newRoom.getRoomName() +  " bola vytvorená");
        }
        throw new NullPointerException("Nebolo možné vytvoriť miestnosť");
    }

    /** Metoda pre ulozenie miestnosti
     * @param room miestnost
     * @return Room ulozena miestnost
     */
    @Override
    public Room saveRoom(Room room) {
       return this.roomRepository.save(room);
    }

    /** Metoda pre zistenie, ci existuje aktivna miestnost s danym kodom
     * @param roomPasscode pristupovy kod
     * @return boolean najdena miestnost=true, nenajdena miestnost=false
     */
    @Override
    public boolean existsRoomByPasscodeAndActive(String roomPasscode) {
        return this.roomRepository.existsByRoomPasscodeAndActive(roomPasscode, true);
    }

    /** Pokusi sa najst miestnost podla daneho kodu, ak je najdena premapuje ju na dto
     *  a vrati
     * @param roomPasscode pristupovy kod
     * @return RoomDto najdena miestnost
     * @throws NullPointerException ak nebola najdena miestnost
     */
    @Override
    public RoomDto findRoomByPasscodeAndActive(String roomPasscode) {
        Room room = this.roomRepository.findByRoomPasscodeAndActive(roomPasscode,true);
        if (room != null)
        {
            return this.modelMapper.map(room, RoomDto.class);
        }
        throw new NullPointerException("Miestnosť nebola nájdená");
    }

    /** Pokusi sa najst miestnost a jej autora, ak je splnena podmienka a kod sa
     *  aktualne nepouziva, nastavi sa novy kod miestnosti a zmeni sa jej aktivita
     *  nasledne sa ulozi autor miestnosti a vrati sa odpoved o zmene kodu
     * @param roomDto DTO miestnost
     * @return ResponseDto odpoved o zmene pristupoveho kodu miestnosti
     * @throws IllegalArgumentException pokial nebol najdeny autor, miestnost, bol
     * zadany prazdny kod alebo sa kod aktualne pouziva
     */
    @Override
    public ResponseDto updateRoomPasscode(RoomDto roomDto) {
        Room foundRoom = this.roomRepository.findRoomByIdRoom(roomDto.getIdRoom());
        User owner = this.userService.findUserByIdUser(foundRoom.getIdOwner().getIdUser());
        if (foundRoom != null && owner != null && !roomDto.getRoomPasscode().trim().equals("") &&
                (!this.roomRepository.existsByRoomPasscodeAndActive(roomDto.getRoomPasscode(), true))) {
            foundRoom.setRoomPasscode(roomDto.getRoomPasscode());
            foundRoom.setActive(true);
            this.userService.saveUser(owner);
            return new ResponseDto(roomDto.getIdRoom(),
                    "Nový kód je " + foundRoom.getRoomPasscode());
        }
        throw new IllegalArgumentException("Prísupový kód nebolo možné upraviť");
    }

    /** Pokusi sa najst miestnost a jej autora, ak je splnena podmienka nastavi sa
     * zmeni sa aktivita mniestnosti a ulozi sa autor miestnosti
     * @param idRoom ID miestnosti
     * @return ResponseDto odpoved o zmene aktivity miestnosti
     * @throws NullPointerException ak nebol najdeny autor miestnosti alebo miestnost
     */
    @Override
    public ResponseDto updateRoomActivity(Long idRoom) {
        Room foundRoom = this.roomRepository.findRoomByIdRoom(idRoom);
        User owner = this.userService.findUserByIdUser(foundRoom.getIdOwner().getIdUser());
        if (foundRoom != null && owner != null) {
            foundRoom.setActive(!foundRoom.isActive());
            this.userService.saveUser(owner);
            return new ResponseDto(idRoom,
                    foundRoom.isActive() ? "Miestnosť je aktívna" : "Miestnosť je neaktívna");
        }
        throw new NullPointerException("Miestnosť nebola nájdená");
    }

    /** Pokusi sa najst miesntost podla ID a premapuje ju na dto
     * @param idRoom ID miestnosti
     * @return RoomDto najdena miestnost
     * @throws NullPointerException ak nebola najdena miestnost
     */
    @Override
    public RoomDto findRoomDtoByIdRoom(Long idRoom) {
        Room existsRoom = this.roomRepository.findRoomByIdRoom(idRoom);
        if (existsRoom != null) {
            return this.modelMapper.map(existsRoom, RoomDto.class);
        }
        throw new NullPointerException("Miestnosť nebola nájdená");
    }

    /** Metoda pre najdenie miestnosti podla jej ID
     * @param idRoom ID miestnosti
     * @return Room najdena miestnost
     */
    @Override
    public Room findByIdRoom(Long idRoom) {
        return this.roomRepository.findRoomByIdRoom(idRoom);
    }

    /** Najde vsetky miestnosti, premapuje ich na dto a vrati
     * @return Set<RoomDto> set vsetkych miestnosti
     */
    @Override
    public Set<RoomDto> findAllRooms() {
        return this.roomRepository.findAllByOrderByIdRoomDesc()
                .stream()
                .map( room -> modelMapper.map(room, RoomDto.class))
                .collect(Collectors.toSet());
    }

    /** Pokusi sa najst miestnost a jej autora. Pokusi sa odstranit miestnost
     * zo zoznamu autorovych miestnosti a ulozi autora. Vrati odpoved o zmazani
     * @param idRoom ID miestnosti
     * @return ResponseDto odpoved o zmazani miestnosti
     */
    @Override
    public ResponseDto deleteRoom(Long idRoom) {
        Room foundRoom = this.roomRepository.findRoomByIdRoom(idRoom);
        User owner = foundRoom.getIdOwner();
        owner.removeRoom(foundRoom);
        this.userService.saveUser(owner);
        return new ResponseDto(idRoom, "Miestnosť bola vymazaná");
    }

    /** Pokusi sa najst pouzivatela podla ID a najst jeho miestnosti, ktore premapuje
     *  na dto a vrati
     * @param idUser ID pouzivatela
     * @return Set<RoomDto> set vsetkych miestnosti pre daneho pouzivatela
     * @throws NullPointerException ak pouzivatel nebol najdeny
     */
    @Override
    public Set<RoomDto> findUserRooms(Long idUser) {
        User roomOwner = this.userService.findUserByIdUser(idUser);
        if (roomOwner != null)
        {
            return roomOwner.getRoomSet()
                    .stream()
                    .map(room -> modelMapper.map(room, RoomDto.class))
                    .collect(Collectors.toSet());
        }
        throw new NullPointerException("Používateľ nebol nájdený");
    }

    /** Metoda pre zistenie, ci je pouzivatel autorom miestnosti
     * @param idUser ID pouzivatela
     * @param idRoom ID miestnosti, s ktorou chce pouzivatel vykonat akciu
     * @return Integer pocet najdenych riadkov pouzivatela
     */
    @Override
    public Integer isUserRoomOwner(Long idUser, Long idRoom) {
        return this.roomRepository.isUserRoomOwner(idUser,idRoom);
    }
}
