package sk.uniza.fri.askfri.security.jwt;

import io.jsonwebtoken.*;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import sk.uniza.fri.askfri.model.dto.login.UserDetailsDto;
import java.util.Date;
import java.util.logging.Logger;

/** Trieda pre generovanie a validaciu JWT
 * zdroj: https://bezkoder.com/spring-boot-jwt-mysql-spring-security-architecture/
 * @version 1.0
 * @since   2021-04-21
 */
@Component
public class JwtService {
    private static final Logger LOGGER = Logger.getLogger( JwtService.class.getName() );

    private final int jwtExpiration = 7200000;
    private final String jwtKey = "askfri-jwt-secret";

    /** Generuje novy JWT na zaklade pouzivatelskych udajov, s danou platnostou podla
     * Do JWT vlozi ID a email pouzivatela
     * @param authentication
     * @return
     */
    public String generateJwtToken(Authentication authentication) {

        UserDetailsDto userPrincipal = (UserDetailsDto) authentication.getPrincipal();
        Date date = new Date();

        return Jwts.builder()
                .setId(userPrincipal.getIdUser())
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtKey)
                .compact();
    }

    /** Vykona validaciu JWT podla podpisoveho kluca
     * @param token
     * @return
     */
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

    /** Vrati email pouzivatela z tokenu
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /** Vrati ID pouzivatela z tokenu
     * @param token
     * @return
     */
    public String getIdFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtKey)
                .parseClaimsJws(token)
                .getBody()
                .getId();
    }
}
