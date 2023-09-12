package com.s3b.helios.oauth2.util;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CookieUtilsTest {

    private static final String COOKIE_NAME = "name";
    private static final String COOKIE_VALUE = "value";
    private static final int COOKIE_MAX_AGE =  30;

    @Test
    void should_RetrieveCookie_When_CookieExists(){
        var request = new MockHttpServletRequest();
        request.setCookies(new Cookie(COOKIE_NAME, COOKIE_VALUE));
        var actual = CookieUtils.getCookie(request, COOKIE_NAME);
        assertTrue(actual.isPresent());
    }

    @Test
    void should_GetDoesNotThrow_When_RequestIsNull(){
        var actual = CookieUtils.getCookie(null, COOKIE_NAME);
        assertTrue(actual.isEmpty());
    }

    @Test
    void should_GetDoesNotThrow_When_CookiesIsNull(){
        var request = new MockHttpServletRequest();
        var actual = CookieUtils.getCookie(request, COOKIE_NAME);
        assertTrue(actual.isEmpty());
    }

    @Test
    void should_CookieAdded_When_TryToAddCookie(){
        var resp = new MockHttpServletResponse();
        CookieUtils.addCookie(resp, COOKIE_NAME, COOKIE_VALUE, COOKIE_MAX_AGE);
        assertTrue(resp.getCookies().length > 0);
        var cookie = resp.getCookies()[0];
        assertEquals(COOKIE_NAME, cookie.getName());
        assertEquals(COOKIE_VALUE, cookie.getValue());
        assertEquals(COOKIE_MAX_AGE, cookie.getMaxAge());
    }

    @Test
    void should_DeleteDoesNotThrow_When_RequestIsNull(){
        assertDoesNotThrow(() -> CookieUtils.deleteCookie(null, new MockHttpServletResponse(), "key"));
    }

    @Test
    void should_DeleteDoesNotThrow_When_ResponseIsNull(){
        assertDoesNotThrow(() -> CookieUtils.deleteCookie(new MockHttpServletRequest(), null, "key"));
    }

    @Test
    void should_DeleteDoesNotThrow_When_CookieIsNull(){
        assertDoesNotThrow(() -> CookieUtils.deleteCookie(new MockHttpServletRequest(), new MockHttpServletResponse(), null));
    }

    @Test
    void should_DeleteDoesNotThrow_When_CookieIsNotFound(){
        var request = new MockHttpServletRequest();
        request.setCookies(new Cookie(COOKIE_NAME, COOKIE_VALUE));
        assertDoesNotThrow(() -> CookieUtils.deleteCookie(new MockHttpServletRequest(), new MockHttpServletResponse(), "test"));
    }

    @Test
    void should_DeleteCookie_When_CookieIsFound(){
        var request = new MockHttpServletRequest();
        request.setCookies(new Cookie(COOKIE_NAME, COOKIE_VALUE));
        var resp = new MockHttpServletResponse();
        assertDoesNotThrow(() -> CookieUtils.deleteCookie(request, resp, COOKIE_NAME));
        assertNotNull(resp.getCookies());
        assertTrue(resp.getCookies().length > 0);
        var cookie = resp.getCookies()[0];
        assertEquals(COOKIE_NAME, cookie.getName());
        assertTrue(cookie.getValue().isBlank());
        assertEquals(0, cookie.getMaxAge());

    }

}