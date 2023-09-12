package com.s3b.helios.client.token;

/**
 * The reading of a token is handled by the implementations of this interface
 * @author Sébastien SAEZ
 */
public interface HeliosTokenReader {
    /**
     * Verify if a given token is valid.
     * @param token to verify
     * @return <code>true</code> if the token is valid or not
     *         <code>false</code> otherwise.
     */
    boolean validate(String token);

    /**
     * Return the subject stored in the specified token.
     * @param token used to extract the subject
     * @return the subject from the specified token
     */
    String extractSubject(String token);
}
