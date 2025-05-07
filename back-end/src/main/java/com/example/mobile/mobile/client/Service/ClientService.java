package com.example.mobile.mobile.client.Service;

import com.example.mobile.mobile.client.Model.Client;
import com.example.mobile.mobile.client.Repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client ajouterClient(Client client) {
        return clientRepository.save(client);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public void supprimerClient(Long id) {
        clientRepository.deleteById(id);
    }
}
