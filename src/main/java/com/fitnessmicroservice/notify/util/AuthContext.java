package com.fitnessmicroservice.notify.util;

import com.fitnessmicroservice.notify.entity.UserPrincipal;
import com.fitnessmicroservice.notify.exception.custom.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthContext {
    public UserPrincipal getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            return (UserPrincipal) authentication.getPrincipal();
        }
        throw new UserNotFoundException("No authenticated user found");
    }
    public String getIdOfCurrentLoggedInUser(){
        UserPrincipal user = getCurrentLoggedInUser();
        if(user.getUser().getId() != null) return user.getUser().getId();
        else throw new UserNotFoundException("No authenticated user found");
    }
}
