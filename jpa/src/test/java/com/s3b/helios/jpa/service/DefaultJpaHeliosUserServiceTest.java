package com.s3b.helios.jpa.service;

import com.s3b.helios.jpa.entity.HeliosAddressEntity;
import com.s3b.helios.jpa.entity.HeliosUserEntity;
import com.s3b.helios.jpa.repository.DefaultHeliosRepository;
import com.s3b.helios.model.HeliosAddressDto;
import com.s3b.helios.model.HeliosUserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class DefaultJpaHeliosUserServiceTest {

    @Mock
    private DefaultHeliosRepository repository;

    @InjectMocks
    private DefaultJpaHeliosUserService service;

    private static final String SAVED_ADDRESS = "2 Street";
    private static final String SAVED_COMPLEMENT = "B2";
    private static final String SAVED_ZIPCODE = "ZIP";
    private static final String SAVED_CITY = "city";
    private static final String SAVED_COUNTRY = "country";


    private static final String SAVED_SUBJECT = "subject";
    private static final String SAVED_LASTNAME = "lastname";
    private static final String SAVED_FIRSTNAME = "firstname";
    private static final LocalDate SAVED_BIRTHDATE = LocalDate.now();


    @Test
    void should_ReturnUser_when_SubjectIsFound(){
        Mockito.doReturn(Optional.of(buildEntity())).when(repository).findBySubject(SAVED_SUBJECT);
        var actual = this.service.findUserBySubject(SAVED_SUBJECT);
        assertTrue(actual.isPresent());
        assertUser(actual.get());
    }

    @Test
    void should_ReturnEmpty_when_SubjectIsNotFound(){
        Mockito.doReturn(Optional.empty()).when(repository).findBySubject(SAVED_SUBJECT);
        var actual = this.service.findUserBySubject(SAVED_SUBJECT);
        assertTrue(actual.isEmpty());
    }

    @Test
    void should_ReturnEmpty_when_SubjectToUpdateIsNotFound(){
        Mockito.doReturn(Optional.empty()).when(repository).findBySubject(SAVED_SUBJECT);
        var actual = this.service.updateUser(SAVED_SUBJECT, new HeliosUserDto());
        assertTrue(actual.isEmpty());
    }

    @Test
    void should_ReturnSavedUser_when_SubjectToUpdateIsFound(){
        var newFirstName = "new firstname";
        var newUser = new HeliosUserDto();
        newUser.setSubject("should not be updated");
        newUser.setFirstName(newFirstName);
        newUser.setAddress(List.of(new HeliosAddressDto()));

        var captor = ArgumentCaptor.forClass(HeliosUserEntity.class);
        Mockito.doReturn(Optional.of(buildEntity())).when(repository).findBySubject(SAVED_SUBJECT);

        Mockito.doReturn(buildEntity()).when(repository).save(any());

        var actual = this.service.updateUser(SAVED_SUBJECT, newUser);

        Mockito.verify(repository).save(captor.capture());
        var updatedEntity = captor.getValue();

        assertEquals(SAVED_SUBJECT, updatedEntity.getSubject());
        assertEquals(newFirstName, updatedEntity.getFirstName());
        assertEquals(2, updatedEntity.getAddress().size());

        assertTrue(actual.isPresent());
        assertUser(actual.get());
    }

    private HeliosUserEntity buildEntity(){
        var addressEntity = new HeliosAddressEntity();
        addressEntity.setAddress(SAVED_ADDRESS);
        addressEntity.setAddressComplement(SAVED_COMPLEMENT);
        addressEntity.setCity(SAVED_CITY);
        addressEntity.setCountry(SAVED_COUNTRY);
        addressEntity.setZipCode(SAVED_ZIPCODE);

        var userEntity = new HeliosUserEntity();
        userEntity.setSubject(SAVED_SUBJECT);
        userEntity.setLastName(SAVED_LASTNAME);
        userEntity.setFirstName(SAVED_FIRSTNAME);
        userEntity.setBirthdate(SAVED_BIRTHDATE);
        userEntity.setAddress(List.of(addressEntity));

        return userEntity;
    }

    private void assertUser(HeliosUserDto userDto){
        assertEquals(SAVED_SUBJECT, userDto.getSubject());
        assertEquals(SAVED_LASTNAME, userDto.getLastName());
        assertEquals(SAVED_FIRSTNAME, userDto.getFirstName());
        assertEquals(SAVED_BIRTHDATE, userDto.getBirthdate());
        assertEquals(1, userDto.getAddress().size());
        var addressDto = userDto.getAddress().get(0);
        assertEquals(SAVED_ADDRESS, addressDto.getAddress());
        assertEquals(SAVED_COMPLEMENT, addressDto.getAddressComplement());
        assertEquals(SAVED_CITY, addressDto.getCity());
        assertEquals(SAVED_COUNTRY, addressDto.getCountry());
        assertEquals(SAVED_ZIPCODE, addressDto.getZipcode());
    }

}