package ru.ravnasybullin.DoiReg.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



@Service
public class CustomClientDetailsService implements ClientDetailsService {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomClientDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return new ClientRepositoryDetails(passwordEncoder);
    }

    private final static class ClientRepositoryDetails implements ClientDetails {
        private final PasswordEncoder passwordEncoder;

        private static final long serialVersionUID = 1L;

        private ClientRepositoryDetails(PasswordEncoder passwordEncoder) {
            this.passwordEncoder = passwordEncoder;
        }

        @Override
        public String getClientId() {
            return "apiOne";
        }

        @Override
        public Set<String> getResourceIds() {
            return null;
        }

        @Override
        public boolean isSecretRequired() {
            return true;
        }

        @Override
        public String getClientSecret() {
            return passwordEncoder.encode("apiOne");
        }

        @Override
        public boolean isScoped() {
            return false;
        }

        @Override
        public Set<String> getScope() {
            return new HashSet<>(Arrays.asList("user_info"));
        }

        @Override
        public Set<String> getAuthorizedGrantTypes() {
            return new HashSet<>(Arrays.asList("password", "refresh_token"));
        }

        @Override
        public Set<String> getRegisteredRedirectUri() {
            return null;
        }

        @Override
        public Collection<GrantedAuthority> getAuthorities() {
            String[] roles = new String[] {"ROLE_ADMIN", "ROLE_OPERATOR", "ROLE_AUDITOR"};
            List<GrantedAuthority> simpleGrantedAuthorityList = new ArrayList<>();
            for (String role : roles) {
                simpleGrantedAuthorityList.add(new SimpleGrantedAuthority(role));
            }
            return simpleGrantedAuthorityList;
        }

        @Override
        public Integer getAccessTokenValiditySeconds() {
            return null;
        }

        @Override
        public Integer getRefreshTokenValiditySeconds() {
            return null;
        }

        @Override
        public boolean isAutoApprove(String scope) {
            return false;
        }

        @Override
        public Map<String, Object> getAdditionalInformation() {
            return null;
        }
    }
}
