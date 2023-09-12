package com.s3b.helios.client.token;

/**
 * The writing of a token is handled by the implementations of this interface
 * @author Sébastien SAEZ
 */
public interface HeliosTokenWriter {
    /**
     * Create a token that has authentication information.
     *
     * @param subject the subject to create the token and store in it
     * @return a generated token
     */
    String generate(String subject);
}
