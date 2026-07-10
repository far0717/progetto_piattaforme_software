package com.parking.parking_system.service;

import com.parking.parking_system.entity.Utente;
import com.parking.parking_system.entity.Veicolo;
import com.parking.parking_system.repository.PrenotazioneRepository;
import com.parking.parking_system.repository.VeicoloRepository;
import com.parking.parking_system.support.dto.DtoVeicoloRequest;
import com.parking.parking_system.support.dto.DtoVeicoloResponse;
import com.parking.parking_system.support.eccezioni.OperazioneNonConsentitaException;
import com.parking.parking_system.support.eccezioni.RisorsaDuplicataException;
import com.parking.parking_system.support.eccezioni.RisorsaNonTrovataException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VeicoloService {

    private final VeicoloRepository repo;
    private final UtenteService utenteService;
    private final PrenotazioneRepository prenotazioneRepository;

    public List<DtoVeicoloResponse> getMieiVeicoli(Jwt jwt) {
        Utente utente = utenteService.getCurrentUser(jwt);
        return repo.findByUtenteOrderByTargaAsc(utente).stream().map(this::map).toList();
    }

    @Transactional
    public DtoVeicoloResponse creaVeicolo(Jwt jwt, DtoVeicoloRequest request) {
        Utente utente = utenteService.getCurrentUser(jwt);
        String targa = request.targa().trim().toUpperCase();
        if (repo.existsByTarga(targa)) {
            throw new RisorsaDuplicataException("Targa già registrata");
        }

        Veicolo veicolo = new Veicolo();
        veicolo.setTarga(targa);
        veicolo.setMarca(request.marca().trim());
        veicolo.setModello(request.modello().trim());
        veicolo.setUtente(utente);

        return map(repo.save(veicolo));
    }

    @Transactional
    public void eliminaMioVeicolo(Jwt jwt, Long id) {
        Utente utente = utenteService.getCurrentUser(jwt);
        Veicolo veicolo = repo.findByIdAndUtente(id, utente)
                .orElseThrow(() -> new RisorsaNonTrovataException(
                        "Veicolo non trovato tra quelli dell'utente "));

        if (prenotazioneRepository.existsByVeicolo(veicolo)) {
            throw new OperazioneNonConsentitaException(
                    "Non puoi eliminare un veicolo che ha prenotazioni associate");
        }

        repo.delete(veicolo);
    }

    public Veicolo getMioVeicolo(Jwt jwt, Long id) {
        Utente utente = utenteService.getCurrentUser(jwt);
        return repo.findByIdAndUtente(id, utente)
                .orElseThrow(() -> new RisorsaNonTrovataException(
                        "Veicolo non trovato tra quelli dell'utente "));
    }

    public Veicolo getMioVeicoloForUpdate(Jwt jwt, Long id) {
        Utente utente = utenteService.getCurrentUser(jwt);
        return repo.findByIdAndUtenteForUpdate(id, utente)
                .orElseThrow(() -> new RisorsaNonTrovataException(
                        "Veicolo non trovato tra quelli dell'utente "));
    }

    public DtoVeicoloResponse map(Veicolo v) {
        return new DtoVeicoloResponse(
                v.getId(),
                v.getTarga(),
                v.getMarca(),
                v.getModello(),
                v.getUtente().getId()
        );
    }
}
