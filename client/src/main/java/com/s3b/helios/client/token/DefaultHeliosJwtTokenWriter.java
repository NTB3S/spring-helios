package com.s3b.helios.client.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * An implementation of {@link HeliosTokenWriter} which provides JWT token writing process
 *
 * @author Sébastien SAEZ
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultHeliosJwtTokenWriter implements HeliosTokenWriter {
    /**
     * The secret to generate a JWT token.
     */
    private final String jwtSecret;

    /**
     * The expiration time defined in hours for the JWT token generated.
     */
    private final int jwtExpirationHours;

    /**
     * {@inheritDoc}
     *<p>
     * The token is built based on the defined secret and {@link SignatureAlgorithm#HS256} :
     *<p> - with the authentication subject
     *<p> - with an expiration time
     * @param subject the subject to create the token and store in it
     * @return a generated token
     */
    @Override
    public String generate(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(jwtExpirationHours, ChronoUnit.HOURS)))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * Create a key based on the JWT secret defined
     * @return a key to build a JWT token
     */
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
