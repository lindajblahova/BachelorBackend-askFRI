package sk.uniza.fri.askfri;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sk.uniza.fri.askfri.model.User;
import sk.uniza.fri.askfri.model.dto.login.UserDetailsDto;
import sk.uniza.fri.askfri.service.IUserService;
import sk.uniza.fri.askfri.service.implementation.UserDetailServiceImplement;

import javax.persistence.EntityManager;

@SpringBootApplication
public class AskfriApplication {

	public static void main(String[] args) {

		SpringApplication.run(AskfriApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(IUserService userService) throws Exception {
		return (String[] args) -> {
			User user = userService.getUserByEmail("adminaskfri@admin.askfri.sk");
			user.setPassword(this.passwordEncoder().encode(user.getPassword()));
			userService.saveUser(user);
		};
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration()
				.setFieldAccessLevel(Configuration.AccessLevel.PUBLIC)
				.setFieldMatchingEnabled(true);
		return modelMapper;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


}
