package com.fitnessmicroservice.notify.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    public void setCookieInTheBrowser(HttpServletResponse response,
                                      String tokenName, String tokenValue, int TTL) {
        try{
//            Now set the cookie in the browser.
            Cookie cookie = new Cookie(tokenName, tokenValue);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(TTL);
            response.addCookie(cookie);
        }catch(Exception ex){
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void deleteCookieFromBrowser(HttpServletResponse response, String tokenName){
        Cookie cookie = new Cookie(tokenName, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // delete immediately
        response.addCookie(cookie);
    }
}
