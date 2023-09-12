package com.s3b.helios.jpa.repository;

import com.s3b.helios.jpa.entity.HeliosUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * An interface to provide a default JPA repository
 * @author Sébastien SAEZ
 */
@Repository
public interface DefaultHeliosRepository extends JpaRepository<HeliosUserEntity, Long> {
    Optional<HeliosUserEntity> findBySubject(String subject);
}
