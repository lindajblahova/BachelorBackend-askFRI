package sk.uniza.fri.askfri.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sk.uniza.fri.askfri.security.jwt.JwtAuthTokenFilter;
import sk.uniza.fri.askfri.security.jwt.UnAuthFilter;
import sk.uniza.fri.askfri.service.implementation.UserDetailServiceImplement;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserDetailServiceImplement detailsServiceimpl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UnAuthFilter unauthorizedHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/*").permitAll()
                .antMatchers("/api/public/*").permitAll()
                .antMatchers("/api/participant-room").permitAll()
                .antMatchers("/api/home").permitAll()
                .antMatchers(HttpMethod.GET,"/api/rooms/*").permitAll()
                .antMatchers(HttpMethod.POST, "/api/**")
                .permitAll().anyRequest()// all other requests need to be authenticated
                .authenticated().and().exceptionHandling()// make sure we use stateless session; session won't be used to
                .authenticationEntryPoint(unauthorizedHandler).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    }


    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(this.detailsServiceimpl);

        return daoAuthenticationProvider;
    }

    @Bean
    JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter();
    }

}
