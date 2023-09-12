package com.s3b.helios.oauth2.service;

import com.s3b.helios.service.HeliosRegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

/**
 * A class to customize the handle of the token obtaining based on Open ID
 * @author Sébastien SAEZ
 */
@RequiredArgsConstructor
@Slf4j
public class HeliosOidcService extends OidcUserService {
    /**
     * A {@link HeliosRegisterService} to process a registration
     */
    private final HeliosRegisterService registerService;

    /**
     * Load the user from the request and register with the {@link  HeliosRegisterService}
     * @param userRequest the user request
     * @return the open id user authenticated
     */
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("The openID token retrieved successfully");
        log.info("The openID user will be load and registered");
        var oidcUser = super.loadUser(userRequest);
        this.registerService.processRegistration(oidcUser.getAttributes());
        return oidcUser;
    }
}
