package com.ecom.userservice.security.controllers;

import com.ecom.userservice.security.dtos.ClientDto;
import com.ecom.userservice.security.models.Client;
import com.ecom.userservice.security.services.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/clients")
public class ClientController {

    ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    ResponseEntity<String> createClient(@RequestBody final ClientDto clientDto) {
        Client client = new Client();
        client.setClientId(clientDto.getClientId());
        client.setClientSecret(clientDto.getClientSecret());
        client.setScopes(clientDto.getScopes());
        client.setRedirectUris(clientDto.getRedirectUris());
        client.setPostLogoutRedirectUris(clientDto.getPostLogoutRedirectUris());
        boolean clientCreated = clientService.registerClient(client);
        if (clientCreated) {
            return ResponseEntity.accepted().body("Client created");
        }
        return ResponseEntity.ok().body("Client not created");
    }
}
