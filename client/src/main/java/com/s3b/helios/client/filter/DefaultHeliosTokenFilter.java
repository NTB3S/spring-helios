package com.s3b.helios.client.filter;

import com.s3b.helios.client.token.HeliosTokenReader;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


/**
 * A Filter based on token authentication behind each http request
 *
 * @see OncePerRequestFilter
 * @see HeliosTokenReader
 * @author Sébastien SAEZ
 */
@RequiredArgsConstructor
@Slf4j
public class DefaultHeliosTokenFilter extends OncePerRequestFilter {
    /**
     * The token  reader used to extract and validate the token
     * @see HeliosTokenReader
     */
    private final HeliosTokenReader tokenReader;

    /**
     * The constant to refer authorization header from http requests
     */
    private static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * The token prefix of an authorization header from http requests
     */
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    /**
     * {@inheritDoc}
     * <p></p>
     * Verify if the token from authorization request is valid to update the security context and let pass the request.
     * Otherwise a forbidden response is returned
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.info("DefaultHeliosTokenFilter processing on the income http request");
        var token = resolveHeaderToken(request);
        if (token.isPresent()
               && tokenReader.validate(token.get())) {
            log.debug("Token is present and valid");
            var username = tokenReader.extractSubject(token.get());
            var authToken = new UsernamePasswordAuthenticationToken(username, "default", List.of());
            SecurityContextHolder.getContext().setAuthentication(authToken);
            log.debug("SecurityContextHolder updated for the username : {}", username);
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    /**
     * Extract the token string after "Bearer " from the http authorization header.
     * @param request the incoming http request
     * @return an {@link Optional} describing the token stored
     */
    private Optional<String> resolveHeaderToken(HttpServletRequest request) {
        var bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        return StringUtils.isNotEmpty(bearerToken) && bearerToken.startsWith(BEARER_TOKEN_PREFIX)
                ? Optional.of(bearerToken.substring(7))
                : Optional.empty();
    }
}
