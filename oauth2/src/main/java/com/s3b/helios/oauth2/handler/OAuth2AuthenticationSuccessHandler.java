package com.s3b.helios.oauth2.handler;

import com.s3b.helios.client.token.HeliosTokenWriter;
import com.s3b.helios.oauth2.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.s3b.helios.oauth2.util.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static com.s3b.helios.oauth2.repository.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;


/**
 * A class to customize the handle of authentication success
 * @author Sébastien SAEZ
 */
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    /**
     * The service to generate a token
     */
    private final HeliosTokenWriter tokenWriter;

    /**
     * The repository for authorization requests based on cookies should be cleaned after handling.
     * @see HttpCookieOAuth2AuthorizationRequestRepository
     */
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    /**
     * Clean authentication and redirect to the target url
     * @param request the request which caused the successful authentication
     * @param response the response
     * @param authentication the <tt>Authentication</tt> object which was created during
     * the authentication process.
     * @see HttpCookieOAuth2AuthorizationRequestRepository
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("The authentication has been successful");
        String targetUrl = determineTargetUrl(request, response, authentication);
        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        log.info("Will be redirect to {}", targetUrl);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);

    }

    /**
     * Return the redirect Uri store in the cookie and add the token query parameter.
     * @param request the http request where to retrieve the redirect uri
     * @param response the response
     * @param authentication the <tt>Authentication</tt> object which was created during
     * the authentication process.
     * @return the target url otherwise "/api"
     *
     * @see HeliosTokenWriter
     * @see HttpCookieOAuth2AuthorizationRequestRepository
     */
    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        var redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse("/api");

        var token = tokenWriter.generate(authentication.getName());
        return UriComponentsBuilder.fromUriString(redirectUri).queryParam("token", token)
                .build().toUriString();
    }

    /**
     * Clean cookies and authentication context
     * @param request the request which caused the successful authentication
     * @param response the response
     * @see HttpCookieOAuth2AuthorizationRequestRepository
     */
    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Cleaning authentication attributes from the request");
        super.clearAuthenticationAttributes(request);
        log.debug("Cleaning the authentication request from cookies");
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}