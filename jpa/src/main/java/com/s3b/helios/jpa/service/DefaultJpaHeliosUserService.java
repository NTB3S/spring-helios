package com.s3b.helios.jpa.service;


import com.s3b.helios.jpa.entity.HeliosAddressEntity;
import com.s3b.helios.jpa.entity.HeliosUserEntity;
import com.s3b.helios.jpa.repository.DefaultHeliosRepository;
import com.s3b.helios.model.HeliosAddressDto;
import com.s3b.helios.model.HeliosUserDto;
import com.s3b.helios.service.HeliosUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * An implementation of {@link HeliosUserService} based on JPA to perform CRUD operations
 *
 * @author Sébastien SAEZ
 */
@RequiredArgsConstructor
@Slf4j
public class DefaultJpaHeliosUserService implements HeliosUserService {

    /**
     * Provide a default JPA repository
     */
    private final DefaultHeliosRepository repository;

    /**
     * {@inheritDoc}
     * @param subject the subject to retrieve a user
     * @return an {@link Optional} the user projection for the specified subject
     *
     * @see HeliosUserDto
     */
    @Override
    public Optional<HeliosUserDto> findUserBySubject(String subject) {
        var optEntity = this.repository.findBySubject(subject);

        if(optEntity.isEmpty()){
            log.info("The user {} is not found", subject);
            return Optional.empty();
        }

        var entity = optEntity.get();
        return buildHeliosUserDto(entity);
    }

    /**
     * {@inheritDoc}
     * @param id the user's id to perform the changes
     * @param heliosUserDto the new user details to save
     * @return the user with his updated details
     *
     * @see HeliosUserDto
     */
    @Override
    public Optional<HeliosUserDto> updateUser(String subject, HeliosUserDto heliosUserDto) {

        var optionalHeliosEntity = this.repository.findBySubject(subject);

        if(optionalHeliosEntity.isEmpty()){
            log.info("The user {} to update is not found", subject);
            return Optional.empty();
        }

        var entity = optionalHeliosEntity.get();
        Optional.ofNullable(heliosUserDto.getFirstName()).ifPresent(entity::setFirstName);
        Optional.ofNullable(heliosUserDto.getLastName()).ifPresent(entity::setLastName);
        Optional.ofNullable(heliosUserDto.getBirthdate()).ifPresent(entity::setBirthdate);
        var addressEntities = new ArrayList<HeliosAddressEntity>();
        Optional.ofNullable(heliosUserDto.getAddress()).ifPresent(addressDtos -> addressDtos.forEach(addrDto ->
                addressEntities.add(
                        new HeliosAddressEntity(addrDto.getAddress(),
                                addrDto.getAddressComplement(),
                                addrDto.getZipcode(),
                                addrDto.getCity() ,
                                addrDto.getCountry(),
                                entity))));
        entity.addAllAddress(addressEntities);

        var saved = repository.save(entity);
        log.info("The user {} updated", subject);
        return buildHeliosUserDto(saved);
    }

    private List<HeliosAddressDto> mapAddress(List<HeliosAddressEntity> addressEntities){
        var addressList = new ArrayList<HeliosAddressDto>();
        addressEntities.forEach(address -> addressList.add(new HeliosAddressDto(address.getId(),
                address.getAddress(),
                address.getAddressComplement(),
                address.getZipCode(),
                address.getCity(),
                address.getCountry())));
        return addressList;
    }

    private Optional<HeliosUserDto> buildHeliosUserDto(HeliosUserEntity entity){
        return Optional.of(new HeliosUserDto(entity.getId(),
                entity.getSubject(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getBirthdate(),
                mapAddress(entity.getAddress())));
    }
}
