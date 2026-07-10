package com.parking.parking_system.repository;

import com.parking.parking_system.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Long> {
    Optional<Utente> findByKeycloakId(String keycloakId);
    Optional<Utente> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByCodiceFiscale(String codiceFiscale);
    boolean existsByEmailAndKeycloakIdNot(String email, String keycloakId);
    boolean existsByCodiceFiscaleAndKeycloakIdNot(String codiceFiscale, String keycloakId);
    //KeycloakIdNot verifica se il keycloakId è diverso da quello indicato da parametro
    //exists By Email And KeycloakId Not verifica se esiste un utente che ha
    //questa email e il cui keycloakId è diverso da quello indicato
}
