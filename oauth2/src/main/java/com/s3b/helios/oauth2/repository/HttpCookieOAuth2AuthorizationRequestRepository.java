package com.s3b.helios.oauth2.repository;

import com.s3b.helios.oauth2.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

/**
 * An implementation of {@link AuthorizationRequestRepository} to store authorization requests in a cookie based repository
 */
@Slf4j
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private static final int COOKIE_EXPIRE_SECONDES = 60;

    /**
     * Retrieve the request from the cookie.
     * @param request the {@code HttpServletRequest}
     * @return the authorization request stored in the cookies
     *
     * @see OAuth2AuthorizationRequest
     */
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        log.info("loadAuthorizationRequest - Retrieve authorization request from the cookies");
        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME).map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    /**
     * Store the authorization request and the redirect uri in the cookies. If the authorization request is null, both cookies are deleted.
     * @param authorizationRequest the {@link OAuth2AuthorizationRequest}
     * @param request the {@code HttpServletRequest}
     * @param response the {@code HttpServletResponse}
     */
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
            CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
            log.info("saveAuthorizationRequest - authorizationRequest is not valid, the cookies had been cleaned");
            return;
        }

        CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtils.serialize(authorizationRequest), COOKIE_EXPIRE_SECONDES);
        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        log.info("saveAuthorizationRequest - Cookie added with expire time of {}", COOKIE_EXPIRE_SECONDES);
        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            log.info("saveAuthorizationRequest - the redirect uri after the logging is set to {}", redirectUriAfterLogin);
            CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, COOKIE_EXPIRE_SECONDES);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }

    /**
     * Clean the redirect uri and the authorization request for the cookie response
     * @param request the {@code HttpServletRequest}
     * @param response the {@code HttpServletResponse}
     */
    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }
}