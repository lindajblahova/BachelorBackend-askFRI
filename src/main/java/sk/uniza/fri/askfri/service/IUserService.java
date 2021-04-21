package sk.uniza.fri.askfri.service;

import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.*;
import sk.uniza.fri.askfri.model.dto.user.UserDto;
import sk.uniza.fri.askfri.model.dto.user.UserPasswordDto;
import sk.uniza.fri.askfri.model.dto.user.UserProfileDto;

import java.util.Set;

public interface IUserService {

    User saveUser(User user);
    ResponseDto createUser(UserDto userDto);
    User getUserByEmail(String email);
    User findUserByIdUser(Long idUser);
    UserProfileDto getUserProfileByIdUser(Long idUser);
    ResponseDto updateUserPassword(UserPasswordDto userPasswordDto);
    Set<UserProfileDto> getAllUsers();
    void deleteUser(Long id);

}
