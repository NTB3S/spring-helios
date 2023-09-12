package com.s3b.helios.client.token;

import io.jsonwebtoken.security.WeakKeyException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultHeliosJwtTokenWriterTest {
    private HeliosTokenWriter tokenWriter = new DefaultHeliosJwtTokenWriter(
            "secretsecretsecretsecretsecretsecretsecretsecret",
            10);

    @Test
    void tokenIsGeneratedSuccessfully() {
        assertDoesNotThrow(() -> tokenWriter.generate("mySubject"));
    }

    @Test
    void should_ThrowWeakKeyEXception_When_KeyIslessThan32bits() {
        tokenWriter = new DefaultHeliosJwtTokenWriter("secretsecretsecretsecretsecrets", 10);
        assertThrows(WeakKeyException.class, () -> tokenWriter.generate("subject"));
    }

}