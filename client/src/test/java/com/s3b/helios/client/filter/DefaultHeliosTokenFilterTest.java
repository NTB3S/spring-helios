package com.s3b.helios.client.filter;

import com.s3b.helios.client.token.HeliosTokenReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class DefaultHeliosTokenFilterTest {

    @Mock
    private HeliosTokenReader tokenReader;

    @InjectMocks
    private DefaultHeliosTokenFilter filter;

    @Test
    void should_Response403_When_TokenIsNotPresent() throws ServletException, IOException {
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        var filterChain = new MockFilterChain();
        this.filter.doFilterInternal(request, response, filterChain);
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());

    }

    @Test
    void should_Response403_When_TokenIsNotValid() throws ServletException, IOException {
        var request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer test");
        var response = new MockHttpServletResponse();
        var filterChain = new MockFilterChain();
        this.filter.doFilterInternal(request, response, filterChain);
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }

    @Test
    void should_ContinueAndUpdateAuthentication_When_TokenIsNotValid() throws ServletException, IOException {
        var request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMDkyODY1NzIyMzIzMjY1MTU1NDciLCJpYXQiOjE2OTU0ODg1MjQsImV4cCI6Mzg1NTQ4ODUyNH0.6as6pTV06LbhkAPfR6hGnJ-0K2BgH6TPuwHhUVj3g2k");
        var response = new MockHttpServletResponse();
        var filterChain = Mockito.mock(MockFilterChain.class);

        Mockito.doReturn(true).when(tokenReader).validate(any());
        Mockito.doReturn("subject").when(tokenReader).extractSubject(any());
        Mockito.doNothing().when(filterChain).doFilter(any(), any());
        this.filter.doFilterInternal(request, response, filterChain);
        Mockito.verify(filterChain).doFilter(any(), any());
        var principal = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        assertEquals("subject", principal.getPrincipal());

    }

}