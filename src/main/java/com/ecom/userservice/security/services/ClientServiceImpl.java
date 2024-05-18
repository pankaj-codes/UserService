package com.ecom.userservice.security.services;

import com.ecom.userservice.security.models.Client;
import com.ecom.userservice.security.repositories.ClientRepository;
import com.ecom.userservice.security.repositories.JpaRegisteredClientRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Primary
public class ClientServiceImpl implements ClientService {

    public static final String DEFAULT_CLIENT_ID = "default-client";
    public static final String DEFAULT_CLIENT_SECRET = "$2a$12$2Dq2xgLYGxvU7SQhfj9OhOxtaK8zLGuVk37v2LSX6t4QtzGtpSYLW";//password = {noop}secret - {noop is not included in password}
    public static final String DEFAULT_REDIRECT_URI = "dhttps://oauth.pstmn.io/v1/callback";
    public static final String DEFAULT_POST_LOGOUT_REDIRECT_URI = "https://oauth.pstmn.io/v1/callback";
    public static final String DEFAULT_ROLE = "ADMIN";
    private final JpaRegisteredClientRepository jpaRegisteredClientRepository;

    JpaRegisteredClientRepository registeredClientRepository;
    ClientRepository clientRepository;

    public ClientServiceImpl(JpaRegisteredClientRepository jpaRegisteredClientRepository, ClientRepository clientRepository) {
        this.jpaRegisteredClientRepository = jpaRegisteredClientRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Boolean registerClient(Client client) {
        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri(client.getRedirectUris())
                .postLogoutRedirectUri(client.getPostLogoutRedirectUris())
                .scope(client.getScopes()) // Roles
                .scope(OidcScopes.PROFILE)
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();
//        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
//                .clientId("oidc-client")
//                .clientSecret("$2a$12$2Dq2xgLYGxvU7SQhfj9OhOxtaK8zLGuVk37v2LSX6t4QtzGtpSYLW")//Password = secret
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .redirectUri("https://oauth.pstmn.io/v1/callback")
//                .postLogoutRedirectUri("https://oauth.pstmn.io/v1/callback")
//                .scope(OidcScopes.OPENID) // Roles
//                .scope(OidcScopes.PROFILE)
//                .scope("ADMIN")
////                .scope("MENTOR")
////                .scope("TA")
//                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
//                .build();

        jpaRegisteredClientRepository.save(oidcClient);
        return true;
    }

    @Override
    public Boolean registerDefaultClient() {

        Optional<Client> clientOptional = clientRepository.findByClientId(DEFAULT_CLIENT_ID);
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            if (client.getClientSecret() != null && !client.getClientSecret().isEmpty()) {
                return true;
            }
        }
        Client newClient = new Client();
        newClient.setClientId(DEFAULT_CLIENT_ID);
        newClient.setClientSecret(DEFAULT_CLIENT_SECRET);
        newClient.setRedirectUris(DEFAULT_REDIRECT_URI);
        newClient.setPostLogoutRedirectUris(DEFAULT_POST_LOGOUT_REDIRECT_URI);
        newClient.setScopes(DEFAULT_ROLE);
        return registerClient(newClient);
    }
}
