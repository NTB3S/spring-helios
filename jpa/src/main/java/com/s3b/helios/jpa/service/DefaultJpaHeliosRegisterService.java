package com.s3b.helios.jpa.service;


import com.s3b.helios.jpa.entity.HeliosUserEntity;
import com.s3b.helios.jpa.repository.DefaultHeliosRepository;
import com.s3b.helios.service.HeliosRegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.s3b.helios.constant.GoogleAttributesConstant.FAMILY_NAME_KEY;
import static com.s3b.helios.constant.GoogleAttributesConstant.GIVEN_NAME_KEY;
import static com.s3b.helios.constant.GoogleAttributesConstant.SUBJECT_KEY;

/**
 * An implementation of {@link HeliosRegisterService} based on JPA
 *
 * @author Sébastien SAEZ
 */
@RequiredArgsConstructor
@Slf4j
public class DefaultJpaHeliosRegisterService implements HeliosRegisterService {

    /**
     * Provide a default JPA repository
     */
    private final DefaultHeliosRepository repository;

    /**
     * {@inheritDoc}
     * Store the attribues mapped to a user t
     * @param attributes the user's information of the End-User
     */
    @Override
    public void processRegistration(Map<String, Object> attributes) {
        log.info("Processing to a new registration");
        var subject = String.valueOf(attributes.get(SUBJECT_KEY));
        var firstName = String.valueOf(attributes.get(GIVEN_NAME_KEY));
        var lastName = String.valueOf(attributes.get(FAMILY_NAME_KEY));
        var entity = new HeliosUserEntity(subject, firstName, lastName);
        this.repository.findBySubject(subject).ifPresentOrElse(
                e -> log.info("The user already exists {}", e.getSubject()),
                () -> {
                    this.repository.save(entity);
                    log.info("Registration saved with success : {}", subject);
                });
    }
}
