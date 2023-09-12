package com.s3b.helios.oauth2.entrypoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * An implementation of {@link AuthenticationEntryPoint} to commence an authentication scheme
 */
@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * {@inheritDoc}
     * @param httpServletRequest that resulted in an <code>AuthenticationException</code>
     * @param httpServletResponse so that the user agent can begin authentication
     * @param e that caused the invocation
     * @throws IOException
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        log.error("Responding with unauthorized error. Message - {}", e.getMessage());
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getLocalizedMessage());
    }
}