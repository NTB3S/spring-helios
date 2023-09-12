package com.s3b.helios.oauth2.handler;

import com.s3b.helios.oauth2.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.s3b.helios.oauth2.util.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
/**
 * A class to customize the handle of authentication failure
 * @author Sébastien SAEZ
 */
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    /**
     * The repository for authorization requests based on cookies should be cleaned after handling.
     * @see HttpCookieOAuth2AuthorizationRequestRepository
     */
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    /**
     * Default behaviour for unsuccessful authentication.
     * <ol>
     * <li>Retrieve redirect url from cookies</li>
     * <li>Build the new error url with query param</li>
     * <li>Clean cookies</li>
     * <li>Perform the redirect to the determined url otherwise redirect to "/api"</li>
     * </ol>
     * @param request the request during which the authentication attempt occurred.
     * @param response the response.
     * @param exception the exception which was thrown to reject the authentication
     * request.
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        log.info("The authentication failed");
        var targetUrlFromCookie = CookieUtils.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(("/api"));

        log.info("Will be redirect to {}", targetUrlFromCookie);
        var targetUrl = UriComponentsBuilder.fromUriString(targetUrlFromCookie)
                .queryParam("error", exception.getLocalizedMessage())
                .build().toUriString();

        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
        log.info("The authorization request has been removed from the cookies");
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}

