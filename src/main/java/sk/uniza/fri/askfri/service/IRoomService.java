package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.Room;
import sk.uniza.fri.askfri.model.dto.ResponseDto;
import sk.uniza.fri.askfri.model.dto.RoomDto;

import java.util.Set;

/** Interface pre sluzbu pracujucu s Room
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
public interface IRoomService {

    /** Metoda pre vytvorenie miestnosti
     * @param roomDto DTO miestnost
     * @return ResponseDto odpoved o vytvoreni miestnosti
     * @throws NullPointerException ak nebol najdeny autor alebu udaje boli zle
     */
    ResponseDto createRoom(RoomDto roomDto);

    /** Metoda pre ulozenie miestnosti
     * @param room miestnost
     * @return Room ulozena miestnost
     */
    Room saveRoom(Room room);

    /** Metoda pre zistenie, ci exisuje aktivna miestnost s danym kodom
     * @param roomPasscode pristupovy kod
     * @return boolean najdena miestnost=true, nenajdena miestnost=false
     */
    boolean existsRoomByPasscodeAndActive(String roomPasscode);

    /** Metoda pre najdenie aktivnej miestnosti s danym kodom
     * @param roomPasscode pristupovy kod
     * @return RoomDto najdena miestnost
     * @throws NullPointerException ak nebola najdena miestnost
     */
    RoomDto findRoomByPasscodeAndActive(String roomPasscode);

    /** Metoda pre upravenie pristupoveho kodu miestnosti
     * @param roomDto DTO miestnost
     * @return ResponseDto odpoved o zmene pristupoveho kodu miestnosti
     * @throws IllegalArgumentException pokial nebol najdeny autor, miestnost, bol
     * zadany prazdny kod alebo sa kod aktualne pouziva
     */
    ResponseDto updateRoomPasscode(RoomDto roomDto);

    /** Metoda pre upravenie aktivity miestnosti
     * @param idRoom ID miestnosti
     * @return ResponseDto odpoved o zmene aktivity miestnosti
     * @throws NullPointerException ak nebol najdeny autor miestnosti alebo miestnost
     */
    ResponseDto updateRoomActivity(Long idRoom);

    /** Metoda pre najdenie DTO miestnosti podla jej ID
     * @param idRoom ID miestnosti
     * @return RoomDto najdena miestnost
     * @throws NullPointerException ak nebola najdena miestnost
     */
    RoomDto findRoomDtoByIdRoom(Long idRoom);

    /** Metoda pre najdenie miestnosti podla jej ID
     * @param idRoom ID miestnosti
     * @return Room najdena miestnost
     */
    Room findByIdRoom(Long idRoom);

    /** Metoda pre najdenie setu vsetkych miestnosti
     * @return Set<RoomDto> set vsetkych miestnosti
     */
    Set<RoomDto> findAllRooms();

    /** Metoda pre zmazanie miestnosti
     * @param idRoom ID miestnosti
     * @return ResponseDto odpoved o zmazani miestnosti
     */
    ResponseDto deleteRoom(Long idRoom);

    /** Metoda pre najdenie setu vsetkych miestnosti pre daneho pouzivatela
     * @param idUser ID pouzivatela
     * @return Set<RoomDto> set vsetkych miestnosti pre daneho pouzivatela
     * @throws NullPointerException ak pouzivatel nebol najdeny
     */
    Set<RoomDto> findUserRooms(Long idUser);

    /** Metoda pre zistenie, ci je pouzivatel autorom miestnosti
     * @param idUser ID pouzivatela
     * @param idRoom ID miestnosti, s ktorou chce pouzivatel vykonat akciu
     * @return Integer pocet najdenych riadkov pouzivatela
     */
    Integer isUserRoomOwner(Long idUser, Long idRoom);
}
