package com.example.mobile.mobile.client.Controller;

import com.example.mobile.mobile.client.Model.Client;
import com.example.mobile.mobile.client.Service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/ajouter")
    public Client ajouterClient(@RequestBody Client client) {
        return clientService.ajouterClient(client);
    }

    @GetMapping("/all")
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    public Optional<Client> getClientById(@PathVariable Long id) {
        return clientService.getClientById(id);
    }

    @DeleteMapping("/supprimer/{id}")
    public String supprimerClient(@PathVariable Long id) {
        clientService.supprimerClient(id);
        return "Client supprimé avec succès.";
    }
}
