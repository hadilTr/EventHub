package com.example.mobile.mobile.client.Repository;

import com.example.mobile.mobile.client.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
