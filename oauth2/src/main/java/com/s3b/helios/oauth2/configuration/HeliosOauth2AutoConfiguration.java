package com.s3b.helios.oauth2.configuration;

import com.s3b.helios.client.token.DefaultHeliosJwtTokenReader;
import com.s3b.helios.client.token.DefaultHeliosJwtTokenWriter;
import com.s3b.helios.client.token.HeliosTokenReader;
import com.s3b.helios.client.token.HeliosTokenWriter;
import com.s3b.helios.oauth2.handler.OAuth2AuthenticationFailureHandler;
import com.s3b.helios.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import com.s3b.helios.oauth2.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.s3b.helios.oauth2.service.HeliosOidcService;
import com.s3b.helios.service.HeliosRegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;

/**
 * A container class registering beans used for helios Oauth2 configuration.
 *
 * @author Sébastien SAEZ
 *
 */
@RequiredArgsConstructor
@Slf4j
public class HeliosOauth2AutoConfiguration {

    /**
     * Create bean with a JWT implementation of {@link HeliosTokenReader} if it does not exist yet
     *
     * @param jwtSecret the secret key to generate a JWT token
     * @return the {@link DefaultHeliosJwtTokenReader} for further information
     * @see HeliosTokenReader
     */
    @Bean
    @ConditionalOnMissingBean(HeliosTokenWriter.class)
    public HeliosTokenWriter jwtTokenWriter(@Value("${application.token-provider.jwt.jwtSecret}") String jwtSecret,
                                            @Value("${application.token-provider.jwt.expiration}") int expirationTimeInHours){
        log.info("HeliosTokenProvider implementation is missing, the default one will be create");
        return new DefaultHeliosJwtTokenWriter(jwtSecret, expirationTimeInHours);
    }

    /**
     * Create bean with a default implementation of {@link OidcUserService}
     * @param registerService a service that handles user registration
     * @return the open id connect service used and called after an access token is obtained from the OpenId provider
     *
     * @see HeliosOidcService
     * @see OidcUserService
     */
    @Bean
    public OidcUserService heliosOidcService(HeliosRegisterService registerService){
        log.info("HeliosOidcService is created");
        return new HeliosOidcService(registerService);
    }

    /**
     * Create a bean implementation {@link AuthorizationRequestRepository} to store and retrieve oauth2 state from cookies
     * @return a {@link AuthorizationRequestRepository} based on cookies
     *
     * @see HttpCookieOAuth2AuthorizationRequestRepository
     */
    @Bean
    HttpCookieOAuth2AuthorizationRequestRepository httpCookieAuthReqRepo(){
        log.info("OAuth2 cookie http based is created");
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    /**
     * Create a bean to handle Oauth2 success authentication
     * @param httpCookieAuthReqRepo the authorization request repository based on cookie implementation
     * @param heliosTokenWriter a service to manipulate token
     * @return a success oauth2 authentication handler {@link OAuth2AuthenticationSuccessHandler}
     *
     * @see HttpCookieOAuth2AuthorizationRequestRepository
     * @see HeliosTokenWriter
     */
    @Bean
    public OAuth2AuthenticationSuccessHandler authenticationSuccessHandler(HttpCookieOAuth2AuthorizationRequestRepository httpCookieAuthReqRepo,
                                                                           HeliosTokenWriter heliosTokenWriter){
        log.info("Defining the OAuth2AuthenticationSuccessHandler");
        return new OAuth2AuthenticationSuccessHandler(heliosTokenWriter, httpCookieAuthReqRepo);
    }

    /**
     * Create a bean to handle Oauth2 failure authentication
     * @param httpCookieAuthReqRepo the authorization request repository based on cookie implementation
     * @return a failure oauth2 authentication handler {@link OAuth2AuthenticationFailureHandler}
     *
     * @see HttpCookieOAuth2AuthorizationRequestRepository
     */
    @Bean
    public OAuth2AuthenticationFailureHandler auth2AuthenticationFailureHandler(HttpCookieOAuth2AuthorizationRequestRepository httpCookieAuthReqRepo){
        log.info("Defining the OAuth2AuthenticationFailureHandler");
        return new OAuth2AuthenticationFailureHandler(httpCookieAuthReqRepo);
    }
}
