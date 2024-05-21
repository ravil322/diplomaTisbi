package ru.ravnasybullin.DoiReg.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.ravnasybullin.DoiReg.models.User;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by farkhad.bikmuratov on 25.04.2020.
 */

@Service
public class CustomSecurityService {

    private Authentication getAuthentication() {
        final SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }
        return context.getAuthentication();
    }

    private <T> Collection<T> getAuthorities(Class<T> clazz) {
        final Authentication authentication = getAuthentication();
        if (authentication == null) {
            return new ArrayList<>();
        }
        @SuppressWarnings("uncheked")
        Collection<T> authorities = (Collection<T>) authentication.getAuthorities();

        return authorities;
    }

    public User getUserFromAuthentication() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        return (User) principal;
    }




}
