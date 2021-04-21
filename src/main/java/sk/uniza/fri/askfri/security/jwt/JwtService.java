package sk.uniza.fri.askfri.security.jwt;

import io.jsonwebtoken.*;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import sk.uniza.fri.askfri.model.dto.login.UserDetailsDto;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class JwtService {
    private static final Logger LOGGER = Logger.getLogger( JwtService.class.getName() );

    private final int jwtExpiration = 6300000;
    private final String jwtKey = "askfri-jwt-secret";

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

        } catch (UnsupportedJwtException e) {
            LOGGER.warning("Nepodporovany token:" + e);
        } catch (MalformedJwtException e) {
            LOGGER.warning("Neplatný token:" + e);
        } catch (SignatureException e) {
            LOGGER.warning("Neplatný podpis:" + e );
        }  catch (ExpiredJwtException e) {
            LOGGER.warning("Expirovany token:" + e);
        }  catch (IllegalArgumentException e) {
            LOGGER.warning("JWT string je prázdny:" + e);
        }
        return false;
    }

    public String getUserNameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
