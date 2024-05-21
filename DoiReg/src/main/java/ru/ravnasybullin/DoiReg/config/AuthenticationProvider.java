package ru.ravnasybullin.DoiReg.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.ravnasybullin.DoiReg.models.User;
import ru.ravnasybullin.DoiReg.repositories.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Order(1)
@Component
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationProvider.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        Map<String, String> details = new HashMap<>();
        if (authentication.getDetails() != null && authentication.getDetails() instanceof Map) {
            details.putAll((Map) authentication.getDetails());
        }
        User user;
        try {
            user = userRepository.findByLogin(name);
        } catch (Exception e) {
            throw new AuthenticationServiceException("Ошибка при получении пользователя", e);
        }
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Пароли не совпадают");
        }

        return new UsernamePasswordAuthenticationToken(user, password, new ArrayList<>());

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}