package com.s3b.helios.service;

import java.util.Map;

/**
 * Implementations of this interface are responsible for processing a user registration
 *
 * @author Sébastien SAEZ
 */
public interface HeliosRegisterService {
    /**
     * Perform the user registration after obtaining the user attributes of the End-User
     * @param attributes the user's information of the End-User
     */
    void processRegistration(Map<String, Object> attributes);
}
