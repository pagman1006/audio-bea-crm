package com.audiobea.crm.app.core.security.jwt.filter;

import com.audiobea.crm.app.commons.dto.DtoInUser;
import com.audiobea.crm.app.core.security.jwt.service.IJWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.audiobea.crm.app.utils.Constants.*;
import static com.audiobea.crm.app.utils.ConstantsController.JWT_LOGIN_PATH;
import static com.audiobea.crm.app.utils.ConstantsLog.LOG_OBJECT_REQUEST;
import static com.audiobea.crm.app.utils.ConstantsLog.LOG_START_DESERIALIZATION;
import static org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher.withDefaults;

@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final PathPatternRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            withDefaults().matcher(HttpMethod.POST, JWT_LOGIN_PATH);
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
                log.debug(LOG_START_DESERIALIZATION);
                log.debug(LOG_OBJECT_REQUEST, request.getInputStream());
                user = new ObjectMapper().readValue(request.getInputStream(), DtoInUser.class);
                username = user.getUsername();
                password = user.getPassword();
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
        final String token = jwtService.create(authResult);

        response.addHeader(HEADER_STRING, TOKEN_PREFIX.concat(token));

        Map<String, Object> body = new HashMap<>();
        body.put(TOKEN, token);
        body.put(USER, authResult.getPrincipal());
        body.put(MESSAGE, String.format(MESSAGE_BODY,
                ((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername()));
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(200);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed)
            throws IOException {
        Map<String, Object> body = new HashMap<>();
        body.put(MESSAGE, MESSAGE_BODY_ERROR);
        body.put(ERROR, failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

}
