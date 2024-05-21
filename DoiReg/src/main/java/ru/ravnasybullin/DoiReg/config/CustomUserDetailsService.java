package ru.ravnasybullin.DoiReg.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ravnasybullin.DoiReg.models.User;
import ru.ravnasybullin.DoiReg.repositories.UserRepository;
import ru.ravnasybullin.DoiReg.services.CustomSecurityService;

import java.util.ArrayList;
import java.util.Collection;



@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final CustomSecurityService customSecurityService;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, CustomSecurityService customSecurityService) {
        this.userRepository = userRepository;
        this.customSecurityService = customSecurityService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s does not exist!", username));
        }
        return new UserRepositoryUserDetails(user);
    }

    public UserDetails me() {
        String userLogin = customSecurityService.getUserFromAuthentication().getLogin();
        return loadUserByUsername(userLogin);
    }

    private final static class UserRepositoryUserDetails extends User implements UserDetails {

        private static final long serialVersionUID = 1L;

        private UserRepositoryUserDetails(User user) {
            super(user);
        }


        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return new ArrayList<>();
        }

        @Override
        public String getUsername() {
            return getLogin();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

    }

}
