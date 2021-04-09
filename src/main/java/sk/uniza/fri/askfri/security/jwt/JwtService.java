package sk.uniza.fri.askfri.security.jwt;

import io.jsonwebtoken.*;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sk.uniza.fri.askfri.model.dto.UserDetailsDto;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class JwtService {
    private static final Logger LOGGER = Logger.getLogger( JwtService.class.getName() );

    private final int jwtExpiration = 3600000;
    private final String jwtKey = "secret";

    public String generateJwtToken(Authentication authentication) {

        UserDetailsDto userPrincipal = (UserDetailsDto) authentication.getPrincipal();

        Date date = new Date();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtKey)
                .compact();
    }

    public Boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            LOGGER.warning("Invalid JWT signature -> Message: { " + e + " } ");
        } catch (MalformedJwtException e) {
            LOGGER.warning("Invalid JWT token -> Message: { " + e + " }");
        } catch (ExpiredJwtException e) {
            LOGGER.warning("Expired JWT token -> Message: { " + e + " }");
        } catch (UnsupportedJwtException e) {
            LOGGER.warning("Unsupported JWT token -> Message: { " + e + " }");
        } catch (IllegalArgumentException e) {
            LOGGER.warning("JWT claims string is empty -> Message: { " + e + " }");
        }

        return false;
    }

    public String getUserNameFromToke(String token) {
        return Jwts.parser()
                .setSigningKey(jwtKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
