package com.s3b.helios.jpa.configuration;

import com.s3b.helios.jpa.repository.DefaultHeliosRepository;
import com.s3b.helios.jpa.service.DefaultJpaHeliosRegisterService;
import com.s3b.helios.jpa.service.DefaultJpaHeliosUserService;
import com.s3b.helios.service.HeliosRegisterService;
import com.s3b.helios.service.HeliosUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A container class registering beans used for helios JPA configuration.
 *
 * @author Sébastien SAEZ
 *
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class HeliosJpaAutoConfiguration {
    private final DefaultHeliosRepository repository;

    /**
     * Create bean with a JPA implementation of {@link HeliosRegisterService} if it does not exist yet
     *
     * @return the default JPA implementation
     * @see DefaultJpaHeliosRegisterService
     */
    @Bean
    @ConditionalOnMissingBean(type = "HeliosRegisterService")
    public HeliosRegisterService heliosJpaRegisterService(){
        log.info("HeliosRegisterService implementation is missing, the default one will be create");
        return new DefaultJpaHeliosRegisterService(repository);
    }

    /**
     * Create bean with a JPA implementation of {@link HeliosUserService} if it does not exist yet
     *
     * @return the default JPA implementation
     * @see DefaultJpaHeliosUserService
     */
    @Bean
    @ConditionalOnMissingBean(type = "HeliosUserService")
    public HeliosUserService heliosJpaUserService(){
        log.info("HeliosUserService implementation is missing, the default one will be create");
        return new DefaultJpaHeliosUserService(repository);
    }
}
