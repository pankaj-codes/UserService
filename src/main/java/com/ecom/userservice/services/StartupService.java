package com.ecom.userservice.services;

import com.ecom.userservice.security.models.Client;
import com.ecom.userservice.security.repositories.ClientRepository;
import com.ecom.userservice.security.repositories.JpaRegisteredClientRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class StartupService {
    public static final String DEFAULT_CLIENT_ID = "default-client";
    public static final String DEFAULT_CLIENT_SECRET = "$2a$12$2Dq2xgLYGxvU7SQhfj9OhOxtaK8zLGuVk37v2LSX6t4QtzGtpSYLW";//password = {noop}secret - {noop is not included in password}
    public static final String DEFAULT_REDIRECT_URI = "dhttps://oauth.pstmn.io/v1/callback";
    public static final String DEFAULT_POST_LOGOUT_REDIRECT_URI = "https://oauth.pstmn.io/v1/callback";
    public static final String DEFAULT_ROLE = "ADMIN";
    JpaRegisteredClientRepository registeredClientRepository;
    ClientRepository clientRepository;

    public StartupService(JpaRegisteredClientRepository registeredClientRepository) {
        this.registeredClientRepository = registeredClientRepository;
    }

    @PostConstruct
    public void onStartup() {
//        Optional<Client> clientOptional = clientRepository.findByClientId(DEFAULT_CLIENT_ID);
//        if (clientOptional.isPresent()) {
//            Client client = clientOptional.get();
//            if (client.getClientSecret() != null && !client.getClientSecret().isEmpty()) {
//                return;
//            }
//            createDefaultClient();
//
//        }
    }

    public void createDefaultClient() {
        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(DEFAULT_CLIENT_ID)
                .clientSecret(DEFAULT_CLIENT_SECRET)
//                .clientSecret("{noop}secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri(DEFAULT_REDIRECT_URI)
                .postLogoutRedirectUri(DEFAULT_POST_LOGOUT_REDIRECT_URI)
//                .scope(OidcScopes.OPENID) // Roles
//                .scope(OidcScopes.PROFILE)
                .scope(DEFAULT_ROLE)
//                .scope("MENTOR")
//                .scope("TA")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();

        registeredClientRepository.save(oidcClient);
    }
}
