package com.marketplace.userprofileservice.repository;

import com.marketplace.userprofileservice.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByUserId(Long userId);
}
