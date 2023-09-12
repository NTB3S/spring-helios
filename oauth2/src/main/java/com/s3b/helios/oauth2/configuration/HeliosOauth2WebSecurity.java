package com.s3b.helios.oauth2.configuration;

import com.s3b.helios.client.filter.DefaultHeliosTokenFilter;
import com.s3b.helios.client.token.HeliosTokenReader;
import com.s3b.helios.oauth2.entrypoint.RestAuthenticationEntryPoint;
import com.s3b.helios.oauth2.handler.OAuth2AuthenticationFailureHandler;
import com.s3b.helios.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import com.s3b.helios.oauth2.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



/**
 * A container class registering {@link SecurityFilterChain} used for helios Oauth2 configuration.
 *
 * @author Sébastien SAEZ
 *
 */
@EnableMethodSecurity
@EnableWebSecurity
@Configuration
@Slf4j
public class HeliosOauth2WebSecurity {

    /**
     * Define the oauth2 spring configuration. Add filter based token too.
     * @param http the http security configuration
     * @param httpCookieAuthReqRepo the authorization request repository based on cookie implementation. Store and retrieve state from oauth2
     * @param authenticationSuccessHandler the handler to redirect when the authentication is a success
     * @param auth2AuthenticationFailureHandler the handler to redirect when the authentication is a failure
     * @param heliosOidcService the open id connect service used and called after an access token is obtained from the OpenId provider
     * @param tokenReader a service to read token
     * @return a {@link SecurityFilterChain} for further information
     * @throws Exception  if an error occurred when building the Object
     *
     * @see HttpCookieOAuth2AuthorizationRequestRepository
     * @see OAuth2AuthenticationFailureHandler
     * @see OAuth2AuthenticationSuccessHandler
     * @see com.s3b.helios.oauth2.service.HeliosOidcService
     * @see OidcUserService
     * @see DefaultHeliosTokenFilter
     */
    @Bean(name = "heliosFilterChain")
    public SecurityFilterChain heliosFilterChain(HttpSecurity http,
                                                 HttpCookieOAuth2AuthorizationRequestRepository httpCookieAuthReqRepo,
                                                 OAuth2AuthenticationSuccessHandler authenticationSuccessHandler,
                                                 OAuth2AuthenticationFailureHandler auth2AuthenticationFailureHandler,
                                                 OidcUserService heliosOidcService,
                                                 HeliosTokenReader tokenReader) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(e -> e.authenticationEntryPoint(new RestAuthenticationEntryPoint()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth2 -> {
                    oauth2.authorizationEndpoint( authorizationEndpoint ->
                        authorizationEndpoint.authorizationRequestRepository(httpCookieAuthReqRepo));
                    oauth2.successHandler(authenticationSuccessHandler);
                    oauth2.failureHandler(auth2AuthenticationFailureHandler);
                    oauth2.userInfoEndpoint(userEndpoint -> userEndpoint.oidcUserService(heliosOidcService));
                });
        log.info("OAuth2 configuration is defined");
        http.addFilterBefore(new DefaultHeliosTokenFilter(tokenReader), UsernamePasswordAuthenticationFilter.class);
        log.info("DefaultHeliosTokenFilter added before the UsernamePasswordAuthenticationFilter");
        return http.build();
    }
}
