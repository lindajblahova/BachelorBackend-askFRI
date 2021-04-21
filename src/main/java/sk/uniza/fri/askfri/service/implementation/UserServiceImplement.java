package sk.uniza.fri.askfri.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.dao.*;
import sk.uniza.fri.askfri.model.*;
import sk.uniza.fri.askfri.model.dto.*;
import sk.uniza.fri.askfri.model.dto.user.UserDto;
import sk.uniza.fri.askfri.model.dto.user.UserPasswordDto;
import sk.uniza.fri.askfri.model.dto.user.UserProfileDto;
import sk.uniza.fri.askfri.service.IUserService;

import javax.persistence.EntityExistsException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImplement implements IUserService {

    private final IUserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImplement(IUserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public ResponseDto createUser(UserDto userDto) {
        User newUser = modelMapper.map(userDto, User.class);
        if (!newUser.getEmail().trim().equals("") && !newUser.getFirstname().trim().equals("") &&
                !newUser.getSurname().trim().equals("") && !newUser.getPassword().trim().equals("") && newUser.getPassword().trim().length() >= 8 ) {
            if (this.userRepository.existsByEmail(userDto.getEmail())) {
                throw new EntityExistsException("Email je už registrovaný");
            }
            newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
            newUser = this.userRepository.save(newUser);
            return new ResponseDto(newUser.getIdUser(), "Pokračujte prihlásením");
        }
        throw new IllegalArgumentException("Používateľa nebolo možné zaregistrovať");
    }

    @Override
    public User getUserByEmail(String email) {
        User existingUser = this.userRepository.findByEmail(email);
        if (existingUser != null) {
            return existingUser;
        }
        throw new NullPointerException("Používateľ nie je registrovaný");
    }

    @Override
    public UserProfileDto getUserProfileByIdUser(Long idUser) {
        User user = this.userRepository.findByIdUser(idUser);
        if (user != null)
        {
            return this.modelMapper.map(user, UserProfileDto.class);
        }
        throw new NullPointerException("Používateľ nebol nájdený!");
    }

    @Override
    public User findUserByIdUser(Long idUser) {
        return this.userRepository.findByIdUser(idUser);
    }

    @Override
    public ResponseDto updateUserPassword(UserPasswordDto userPasswordDto) {
        User foundUser = this.userRepository.findByIdUser(userPasswordDto.getIdUser());
        if (foundUser != null) {
            if (this.passwordEncoder.matches(userPasswordDto.getOldPassword(),foundUser.getPassword())
                    && userPasswordDto.getNewPassword().length() >= 8)
            {
                foundUser.setPassword(this.passwordEncoder.encode(userPasswordDto.getNewPassword()));
                this.userRepository.save(foundUser);
                return new ResponseDto(userPasswordDto.getIdUser(), "Heslo bolo zmenené");
            }

            throw new IllegalArgumentException("Zadané heslo nebolo správne");
        }
        throw new NullPointerException("Používateľ neexistuje");
    }

    @Override
    public Set<UserProfileDto> getAllUsers() {
        return this.userRepository.findAllByOrderBySurnameAsc()
                .stream()
                .map( user -> modelMapper.map(user, UserProfileDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }
}
