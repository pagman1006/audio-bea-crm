package com.audiobea.crm.app.core.security.jwt.filter;

import com.audiobea.crm.app.commons.dto.DtoInUser;
import com.audiobea.crm.app.core.security.jwt.service.IJWTService;
import com.audiobea.crm.app.core.security.jwt.service.impl.JWTServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher.withDefaults;

@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final PathPatternRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            withDefaults().matcher(HttpMethod.POST, "/audio-bea/v1/api/login");
    final private AuthenticationManager authenticationManager;
    final private IJWTService jwtService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, IJWTService jwtService) {
        this.authenticationManager = authenticationManager;
        setRequiresAuthenticationRequestMatcher(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "*");
        if (username == null || password == null) {
            DtoInUser user;
            try {
                logger.debug("Start deserialization of the request");
                log.debug("Object request: {}", request.getInputStream());
                user = new ObjectMapper().readValue(request.getInputStream(), DtoInUser.class);

                username = user.getUsername();
                password = user.getPassword();

                logger.info("Username from request InputStream (raw): " + username);
                logger.info("Password from request InputStream (raw): " + password);

            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        if (StringUtils.isNotEmpty(username)) {
            username = username.trim();
        }

        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException {
        String token = jwtService.create(authResult);

        response.addHeader(JWTServiceImpl.HEADER_STRING, JWTServiceImpl.TOKEN_PREFIX + token);

        Map<String, Object> body = new HashMap<>();
        body.put("token", token);
        body.put("user", authResult.getPrincipal());
        body.put("message", String.format("Hi %s, you have logged in successfully!",
                ((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername()));

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(200);
        response.setContentType("application/json");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed)
            throws IOException {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Authentication error: user or password wrong!");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType("application/json");
    }

}
