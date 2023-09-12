package com.s3b.helios.client.configuration;

import com.s3b.helios.client.filter.DefaultHeliosTokenFilter;
import com.s3b.helios.client.token.HeliosTokenReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * A container class registering beans used for helios client configuration.
 *
 * @author Sébastien SAEZ
 *
 */
@Configuration
@Slf4j
public class HeliosClientAutoConfiguration {

    /**
     * Add {@link DefaultHeliosTokenFilter} before {@link UsernamePasswordAuthenticationFilter} only if a "heliosFilterChain" does not exist yet
     *
     * @param http the http security configuration
     * @param tokenReader a token reader implementation
     * @return a {@link SecurityFilterChain} for further information
     * @throws Exception  if an error occurred when building the Object
     *
     * @see DefaultHeliosTokenFilter
     * @see HttpSecurity
     * @see HeliosTokenReader
     *
     */
    @Bean
    public SecurityFilterChain heliosFilterChain(HttpSecurity http, HeliosTokenReader tokenReader) throws Exception {
        http.addFilterBefore(new DefaultHeliosTokenFilter(tokenReader), UsernamePasswordAuthenticationFilter.class);
        log.debug("[heliosFilterChain] DefaultHeliosTokenFilter added before the UsernamePasswordAuthenticationFilter");
        return http.build();
    }
}
