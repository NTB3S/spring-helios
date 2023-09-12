package com.s3b.helios.client.token;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;

/**
 * An implementation of {@link HeliosTokenReader} which provides JWT token reading process
 *
 * @author Sébastien SAEZ
 */
@RequiredArgsConstructor
@Slf4j
public class DefaultHeliosJwtTokenReader implements HeliosTokenReader {

    /**
     * The secret to generate a JWT token.
     */
    private final String jwtSecret;

    /**
     * {@inheritDoc}
     * @param token used to extract the subject
     * @return the subject from the specified token
     */
    @Override
    public String extractSubject(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * {@inheritDoc}
     * This method always returns immediately. When the token isn't valid, the cause is logged
     * @param authToken to verify
     * @return <code>true</code> if the token is valid or not
     *         <code>false</code> otherwise.
     */
    @Override
    public boolean validate(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage(), e);
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage(), e);
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage(), e);
        } catch (SignatureException e){
            log.error("JWT Invalid signature {}", e.getMessage(), e);
        }

        return false;
    }

    /**
     * Create a key based on the JWT secret defined
     * @return a key to build a JWT token
     */
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
