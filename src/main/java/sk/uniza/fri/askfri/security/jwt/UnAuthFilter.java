package sk.uniza.fri.askfri.security.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** Filter pre neautorizovanu ziadost
 * implementuje AuthenticationEntryPoint
 * zdroj: https://bezkoder.com/spring-boot-jwt-mysql-spring-security-architecture/
 * @version 1.0
 * @since   2021-04-21
 */
@Component
public class UnAuthFilter implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
