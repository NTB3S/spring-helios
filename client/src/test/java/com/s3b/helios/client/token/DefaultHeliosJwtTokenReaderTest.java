package com.s3b.helios.client.token;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(OutputCaptureExtension.class)
class DefaultHeliosJwtTokenReaderTest {

    private final HeliosTokenReader heliosTokenReader = new DefaultHeliosJwtTokenReader("secretsecretsecretsecretsecretsecretsecretsecret");

    @Test
    void subjectIsExtractSuccessfully() {
        var expectedSubject = "mySubject";
        var token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJteVN1YmplY3QiLCJpYXQiOjE2OTU1NTE3MTgsImV4cCI6MTY5NTU4NzcxOH0.x40BrcU68SbIa6hQCsYws0VhIEiP8XqSOco8X5erTkA";
        var actualSubject = heliosTokenReader.extractSubject(token);
        assertEquals(expectedSubject, actualSubject);
    }

    @Test
    void should_ReturnTrue_WhenTokenIsValid() {
        var token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMDkyODY1NzIyMzIzMjY1MTU1NDciLCJpYXQiOjE2OTU0ODg1MjQsImV4cCI6Mzg1NTQ4ODUyNH0.6as6pTV06LbhkAPfR6hGnJ-0K2BgH6TPuwHhUVj3g2k";
        var actual = this.heliosTokenReader.validate(token);
        assertTrue(actual);

    }

    @Test
    void should_ReturnFalseWithExpiredJwtException_WhenTokenIsExpired(CapturedOutput output) {
        var token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMDkyODY1NzIyMzIzMjY1MTU1NDciLCJpYXQiOjE2OTU0NzY2NDQsImV4cCI6MTY5NTQ4MDI0NH0.UDaXXrARseVzZZeKq8R1pzmuoG58gk3GkSGi4WjFFo4";
        var actual = this.heliosTokenReader.validate(token);
        assertFalse(actual);
        assertTrue(output.getOut().contains(ExpiredJwtException.class.getName()));
    }

    @Test
    void should_ReturnFalseWithMalformedException_WhenTokenMalformed(CapturedOutput output) {
        var actual = this.heliosTokenReader.validate("token");
        assertFalse(actual);
        assertTrue(output.getOut().contains(MalformedJwtException.class.getName()));
    }

    @Test
    void should_ReturnFalseWithIllegalArgumentException_WhenTokenIsEmpty(CapturedOutput output) {
        var actual = this.heliosTokenReader.validate("");
        assertFalse(actual);
        assertTrue(output.getOut().contains(IllegalArgumentException.class.getName()));
    }

    @Test
    void should_ReturnFalseWithSignatureException_WhenTokenIsNotValid(CapturedOutput output) {
        var token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMDyODY1NzIyMzIzMjY1MTU1NDciLCJpYXQiOjE2OTU0NzY2NDQsImV4cCI6MTY5NTQ4MDI0NH0.UDaXXrARseVzZZeKq8R1pzmuoG58gk3GkSGi4WjFFo4";
        var actual = this.heliosTokenReader.validate(token);
        assertFalse(actual);
        assertTrue(output.getOut().contains(SignatureException.class.getName()));
    }

}