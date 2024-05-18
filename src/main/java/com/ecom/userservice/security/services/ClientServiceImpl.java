package com.ecom.userservice.security.services;

import com.ecom.userservice.security.models.Client;
import com.ecom.userservice.security.repositories.JpaRegisteredClientRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Primary
public class ClientServiceImpl implements ClientService {

    private final JpaRegisteredClientRepository jpaRegisteredClientRepository;

    public ClientServiceImpl(JpaRegisteredClientRepository jpaRegisteredClientRepository) {
        this.jpaRegisteredClientRepository = jpaRegisteredClientRepository;
    }

    @Override
    public Boolean registerClient(Client client) {
        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(client.getId())
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
}
