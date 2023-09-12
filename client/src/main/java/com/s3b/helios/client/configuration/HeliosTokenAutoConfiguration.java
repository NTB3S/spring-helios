package com.s3b.helios.client.configuration;

import com.s3b.helios.client.token.DefaultHeliosJwtTokenReader;
import com.s3b.helios.client.token.HeliosTokenReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * A container class registering token beans.
 *
 * @author Sébastien SAEZ
 *
 */
@Slf4j
public class HeliosTokenAutoConfiguration {
    /**
     * Create bean with a JWT implementation of {@link HeliosTokenReader} if it does not exist yet
     *
     * @param jwtSecret the secret key to generate a JWT token
     * @return the {@link DefaultHeliosJwtTokenReader} for further information
     * @see HeliosTokenReader
     */
    @Bean
    @ConditionalOnMissingBean(HeliosTokenReader.class)
    public HeliosTokenReader jwtTokenReader(@Value("${application.token-provider.jwt.jwtSecret}") String jwtSecret){
        log.info("HeliosTokenProvider implementation is missing, the default one will be create");
        return new DefaultHeliosJwtTokenReader(jwtSecret);
    }
}
