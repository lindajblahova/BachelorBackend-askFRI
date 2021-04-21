package sk.uniza.fri.askfri.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import sk.uniza.fri.askfri.model.dto.login.UserDetailsDto;
import sk.uniza.fri.askfri.service.implementation.UserDetailServiceImplement;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class JwtAuthTokenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = Logger.getLogger( JwtAuthTokenFilter.class.getName() );

    @Autowired
    private JwtService tokenService;

    @Autowired
    private UserDetailServiceImplement userDetailsServiceimpl;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {

            String jwt = getJwt(httpServletRequest);
            if (jwt != null && tokenService.validateJwtToken(jwt)) {
                String username = tokenService.getUserNameFromToken(jwt);

                UserDetailsDto userDetails = userDetailsServiceimpl.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        } catch (Exception e) {
            LOGGER.warning("Can NOT set user authentication -> Message: { " + e + " }");
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }


    private String getJwt(HttpServletRequest httpServletRequest) {
        String header = httpServletRequest.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            return header.replace("Bearer ", "");
        } else
            return null;
    }
}
