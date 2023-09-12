package com.s3b.helios.oauth2.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.Base64;
import java.util.Optional;

/**
 * Operations based on {@link Cookie}
 */
public final class CookieUtils {

    /**
     * Prevent class instantiation.
     */
    private CookieUtils(){}

    /**
     * Fetch a cookie by his name
     * @param request the {@code HttpServletRequest}
     * @param name the name of a stored cookie
     * @return a {@link Optional} of the retrieved cookie
     *
     * @see Cookie
     */
    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        if(request != null){
            var cookies = request.getCookies();
            if (cookies != null && cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(name)) {
                        return Optional.of(cookie);
                    }
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Add a specified cookie to the specified response
     * @param response the {@code HttpServletResponse}
     * @param name of the cookie to store
     * @param value of the cookie to store
     * @param maxAge the duration of the cookie
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        var cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * Delete the cookie from the specified response by cleaning the value and setting the maxAge to 0
     * @param request the {@code HttpServletRequest}
     * @param response the {@code HttpServletResponse}
     * @param name the name of a stored cookie to delete
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        if(request != null && response != null) {
            var cookies = request.getCookies();
            if (cookies != null && cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(name)) {
                        cookie.setValue("");
                        cookie.setPath("/");
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                    }
                }
            }
        }
    }

    /**
     * Serialize and encode in Base64 the specified serializable
     * @param serializable the object to serialize
     * @return the serialized object
     */
    public static String serialize(Serializable serializable) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(serializable));
    }

    /**
     * Deserialize the specified cookie value to the  specified class
     * @param cookie the cookie to retrieve the value to deserialize
     * @param cls the target class of the deserializable
     * @return the deserialized object
     */
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
    }
}
