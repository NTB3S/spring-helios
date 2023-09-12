package com.s3b.helios.jpa.service;

import com.s3b.helios.jpa.entity.HeliosUserEntity;
import com.s3b.helios.jpa.repository.DefaultHeliosRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(OutputCaptureExtension.class)
@ExtendWith(MockitoExtension.class)
class DefaultJpaHeliosRegisterServiceTest  {

    @Mock
    private DefaultHeliosRepository repository;

    @InjectMocks
    private DefaultJpaHeliosRegisterService service;

    @Test
    void should_SaveToRepositoryWithAttributes_whenProcessingRegistration(){

        var subject = "subject";
        var firstName = "firstName";
        var lastName = "lastName";

        var attributes = new HashMap<String, Object>();
        attributes.put("sub", subject);
        attributes.put("given_name", firstName);
        attributes.put("family_name", lastName);

        var captor = ArgumentCaptor.forClass(HeliosUserEntity.class);

        assertDoesNotThrow(() -> this.service.processRegistration(attributes));

        Mockito.verify(repository).save(captor.capture());
        var savedEntity = captor.getValue();
        assertEquals(subject, savedEntity.getSubject());
        assertEquals(firstName, savedEntity.getFirstName());
        assertEquals(lastName, savedEntity.getLastName());
    }

    @Test
    void should_Log_When_userAlreadyExists(CapturedOutput output){
        Mockito.doReturn(Optional.of(new HeliosUserEntity())).when(repository).findBySubject(any());
        this.service.processRegistration(new HashMap<>());
        assertTrue(output.getOut().contains("The user already exists"));

    }
}