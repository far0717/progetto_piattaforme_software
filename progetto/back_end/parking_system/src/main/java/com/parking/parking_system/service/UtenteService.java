package com.parking.parking_system.service;

import com.parking.parking_system.entity.Utente;
import com.parking.parking_system.repository.UtenteRepository;
import com.parking.parking_system.support.dto.DtoProfiloUtenteRequest;
import com.parking.parking_system.support.dto.DtoUtenteResponse;
import com.parking.parking_system.support.eccezioni.RisorsaDuplicataException;
import com.parking.parking_system.support.eccezioni.RisorsaNonTrovataException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

//Tutti i metodi della classe sono di default eseguiti in una transazione di sola lettura
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UtenteService {

    private final UtenteRepository repo;

    public List<DtoUtenteResponse> getAll() {

        return repo.findAll().stream().map(this::map).toList();
    }//getALL()

    public DtoUtenteResponse getById(Long id) {
        return map(repo.findById(id)
                .orElseThrow(() -> new RisorsaNonTrovataException("Utente con id " + id + " non trovato")));
    }//getById

    public DtoUtenteResponse getMyProfile(Jwt jwt) {

        return map(getCurrentUser(jwt));
    }//getMyProfile()

    public Utente getCurrentUser(Jwt jwt) {
        return repo.findByKeycloakId(jwt.getSubject())
                .orElseThrow(() -> new RisorsaNonTrovataException(
                        "Profilo non completato. Dopo il primo login compila i dati utente."));
    }//getCurrentUser()

    @Transactional
    public DtoUtenteResponse createOrUpdateMyProfile(Jwt jwt, DtoProfiloUtenteRequest request) {
        String keycloakId = jwt.getSubject();
        String email = getRequiredEmail(jwt);
        String codiceFiscale = request.codiceFiscale().trim().toUpperCase();

        if (repo.existsByEmailAndKeycloakIdNot(email, keycloakId)) {
            throw new RisorsaDuplicataException("Email già associata a un altro utente");
        }
        if (repo.existsByCodiceFiscaleAndKeycloakIdNot(codiceFiscale, keycloakId)) {
            throw new RisorsaDuplicataException("Codice fiscale già associato a un altro utente");
        }

        Utente utente = repo.findByKeycloakId(keycloakId).orElseGet(Utente::new);
        utente.setKeycloakId(keycloakId);
        utente.setEmail(email);
        utente.setCodiceFiscale(codiceFiscale);
        utente.setNome(request.nome().trim());
        utente.setCognome(request.cognome().trim());

        return map(repo.save(utente));
    }

    private String getRequiredEmail(Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        if (email == null || email.isBlank()) {
            throw new RisorsaNonTrovataException("Nel token Keycloak manca l'email dell'utente");
        }
        return email.trim().toLowerCase();
    }

    private DtoUtenteResponse map(Utente u) {
        return new DtoUtenteResponse(
                u.getId(),
                u.getKeycloakId(),
                u.getCodiceFiscale(),
                u.getNome(),
                u.getCognome(),
                u.getEmail()
        );
    }
}
