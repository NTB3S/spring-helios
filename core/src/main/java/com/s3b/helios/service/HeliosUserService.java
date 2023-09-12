package com.s3b.helios.service;

import com.s3b.helios.model.HeliosUserDto;

import java.util.Optional;

/**
 * Basic CRUD operations on the user resource are handled by the implementations of this interface.
 * @author Sébastien SAEZ
 */
public interface HeliosUserService {
    /**
     * Fetch a user by subject
     * @param subject the id to retrieve a user
     * @return an {@link Optional} the user projection for the specified subject
     *
     * @see HeliosUserDto
     */
    Optional<HeliosUserDto> findUserBySubject(String subject);

    /**
     * Update a user's details with the new details specified
     * @param subject the user's subject to perform the changes
     * @param heliosUserDto the new user details to save
     * @return  an {@link Optional} user with his updated details
     *
     * @see HeliosUserDto
     */
    Optional<HeliosUserDto> updateUser(String subject, HeliosUserDto heliosUserDto);

}
