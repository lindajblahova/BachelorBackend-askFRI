package sk.uniza.fri.askfri.model.dto.login;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import sk.uniza.fri.askfri.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** Trieda obsahuje potrebne informacie na zostavenie autentifikacneho objektu
 * implementuje UserDetails
 * obsahuje pouzivatela a kolekciu jeho autorít
 * @author Linda Blahova
 * @version 1.0
 * @since   2021-04-21
 */
public class UserDetailsDto implements UserDetails {

    private final User user;
    private final List<String> roles = new ArrayList<String>();

    public UserDetailsDto(User user) {
        this.user = user;
        this.roles.add(this.user.getRole());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        this.roles.forEach( r-> {
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + r);
            authorities.add(authority);
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    public String getIdUser() {
        return this.user.getIdUser().toString();}

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
