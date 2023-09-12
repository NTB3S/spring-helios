package com.s3b.helios.oauth2.controller;

import com.s3b.helios.model.HeliosUserDto;
import com.s3b.helios.service.HeliosUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * The Helios API.
 *
 * @author Sébastien SAEZ
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/helios")
@Slf4j
public class HeliosController {

    /**
     * This is an interface to perform operations
     * @see HeliosUserService
     */
    private final HeliosUserService heliosUserService;

    /**
     * Fetch a user by subject
     * @param subject the specified subject from the path variable
     * @return a {@link ResponseEntity} with a user projection
     *
     * @see HeliosUserDto
     */
    @GetMapping("/{subject}")
    public ResponseEntity<HeliosUserDto> findBySubject(@PathVariable("subject") String subject){
        log.info("Getting user by subject : {}", subject);
        var result = heliosUserService.findUserBySubject(subject)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT));
        return ResponseEntity.ok(result);
    }


    /**
     * Update a user's details with the information passed into the body.
     * The update is performed on the specified id only if it matches the authentication header from the http request.
     *
     * @param subject user subject id to update
     * @param heliosUserDto the new user to save
     * @return a {@link ResponseEntity} of the user with updated details
     *
     * @see HeliosUserDto
     */
    @PreAuthorize("#subject == authentication.principal")
    @PutMapping("/{subject}")
    public ResponseEntity<HeliosUserDto> updateUser(@PathVariable("subject") String subject,
                                                    @RequestBody HeliosUserDto heliosUserDto){
        log.info("Updating the user for the subject : {}", subject);
        var result = heliosUserService.updateUser(subject, heliosUserDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT));
        return ResponseEntity.ok(result);
    }
}

