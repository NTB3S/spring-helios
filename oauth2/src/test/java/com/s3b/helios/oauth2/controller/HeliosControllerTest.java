package com.s3b.helios.oauth2.controller;

import com.s3b.helios.model.HeliosAddressDto;
import com.s3b.helios.model.HeliosUserDto;
import com.s3b.helios.service.HeliosUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class HeliosControllerTest {

    @Mock
    private HeliosUserService service;

    @InjectMocks
    private HeliosController controller;

    @Test
    void should_NoContent_When_UserNotFound() {
        Mockito.when(service.findUserBySubject(any())).thenReturn(Optional.empty());

        var actual = assertThrows(ResponseStatusException.class, () -> controller.findBySubject("subject"));

        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
    }

    @Test
    void should_OkWithDto_When_UserFound() {
        Mockito.when(service.findUserBySubject(any())).thenReturn(Optional.of(buildHeliosDto()));

        var actual = controller.findBySubject("subject");

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertNotNull(actual.getBody());
        assertHeliosDto(actual.getBody());

    }

    @Test
    void should_OkWithDto_When_UserIsUpdated() {
        Mockito.when(service.updateUser(any(), any())).thenReturn(Optional.of(buildHeliosDto()));

        var actual = controller.updateUser("subject", new HeliosUserDto());
        assertEquals(HttpStatus.OK, actual.getStatusCode());

        assertNotNull(actual.getBody());
        assertHeliosDto(actual.getBody());

    }

    @Test
    void should_NoContent_When_UserToUpdateIsNotFound() {
        Mockito.when(service.updateUser(any(), any())).thenReturn(Optional.empty());
        var request = new HeliosUserDto();

        var actual = assertThrows(ResponseStatusException.class, () -> controller.updateUser("subject", request));

        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
    }

    private HeliosUserDto buildHeliosDto(){
        var addressDto = new HeliosAddressDto();
        addressDto.setAddress("Test");
        addressDto.setId(1L);
        addressDto.setAddressComplement("Complement");
        addressDto.setCity("city");
        addressDto.setZipcode("zipcode");
        addressDto.setCountry("country");
        return new HeliosUserDto(1L, "mockedSubject", "firstName", "lastName", LocalDate.now(), List.of(addressDto));
    }

    private void assertHeliosDto(HeliosUserDto actualUser){
        assertEquals(1L, actualUser.getId());
        assertEquals("mockedSubject", actualUser.getSubject());
        assertEquals("firstName", actualUser.getFirstName());
        assertEquals("lastName", actualUser.getLastName());
        assertEquals(LocalDate.now(), actualUser.getBirthdate());

        assertNotNull(actualUser.getAddress());
        assertEquals(1, actualUser.getAddress().size());
        var actualAddress = actualUser.getAddress().get(0);
        assertEquals(1L, actualAddress.getId());
        assertEquals("Test", actualAddress.getAddress());
        assertEquals("Complement", actualAddress.getAddressComplement());
        assertEquals("city", actualAddress.getCity());
        assertEquals("zipcode", actualAddress.getZipcode());
        assertEquals("country", actualAddress.getCountry());

    }

}