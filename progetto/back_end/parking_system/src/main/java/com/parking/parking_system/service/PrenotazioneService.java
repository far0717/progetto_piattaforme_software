package com.parking.parking_system.service;

import com.parking.parking_system.entity.Parcheggio;
import com.parking.parking_system.entity.Prenotazione;
import com.parking.parking_system.entity.Utente;
import com.parking.parking_system.entity.Veicolo;
import com.parking.parking_system.repository.ParcheggioRepository;
import com.parking.parking_system.repository.PrenotazioneRepository;
import com.parking.parking_system.support.dto.DtoPrenotazioneRequest;
import com.parking.parking_system.support.dto.DtoPrenotazioneResponse;
import com.parking.parking_system.support.eccezioni.OperazioneNonConsentitaException;
import com.parking.parking_system.support.eccezioni.ParcheggioNotFoundException;
import com.parking.parking_system.support.eccezioni.ParcheggioOccupatoException;
import com.parking.parking_system.support.eccezioni.RisorsaNonTrovataException;
import com.parking.parking_system.support.validation.PrenotazioneValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PrenotazioneService {

    private final PrenotazioneRepository repo;
    private final ParcheggioRepository parcheggioRepository;
    private final UtenteService utenteService;
    private final VeicoloService veicoloService;
    private final PrenotazioneValidator prenotazioneValidator;

    public List<DtoPrenotazioneResponse> getMiePrenotazioni(Jwt jwt) {
        Utente utente = utenteService.getCurrentUser(jwt);
        return repo.findByUtenteOrderByOrarioInizioDesc(utente).stream().map(this::map).toList();
    }

    public List<DtoPrenotazioneResponse> getAll() {
        return repo.findAllByOrderByOrarioInizioDesc().stream().map(this::map).toList();
    }

    @Transactional
    public DtoPrenotazioneResponse creaPrenotazione(Jwt jwt, DtoPrenotazioneRequest request) {
        prenotazioneValidator.validaIntervallo(request.orarioInizio(), request.orarioFine());

        Utente utente = utenteService.getCurrentUser(jwt);

        Parcheggio parcheggio = parcheggioRepository.findByIdForUpdate(request.parcheggioId())
                .orElseThrow(() -> new ParcheggioNotFoundException(request.parcheggioId()));
        Veicolo veicolo = veicoloService.getMioVeicoloForUpdate(jwt, request.veicoloId());

        if (!parcheggio.isDisponibile()) {
            throw new ParcheggioOccupatoException(parcheggio.getNumero());
        }

        if (repo.existsSovrapposizioneParcheggio(parcheggio.getId(), request.orarioInizio(), request.orarioFine())) {
            throw new ParcheggioOccupatoException(parcheggio.getNumero());
        }

        if (repo.existsSovrapposizioneVeicolo(veicolo.getId(), request.orarioInizio(), request.orarioFine())) {
            throw new OperazioneNonConsentitaException(
                    "Il veicolo selezionato ha già una prenotazione nella stessa fascia oraria");
        }

        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setUtente(utente);
        prenotazione.setVeicolo(veicolo);
        prenotazione.setParcheggio(parcheggio);
        prenotazione.setOrarioInizio(request.orarioInizio());
        prenotazione.setOrarioFine(request.orarioFine());
        prenotazione.setData(request.orarioInizio().toLocalDate());

        return map(repo.save(prenotazione));
    }

    @Transactional
    public void cancellaMiaPrenotazione(Jwt jwt, Long id) {
        Utente utente = utenteService.getCurrentUser(jwt);
        Prenotazione prenotazione = repo.findByIdAndUtente(id, utente)
                .orElseThrow(() -> new RisorsaNonTrovataException(
                        "Prenotazione non trovata tra quelle dell'utente"));

        repo.delete(prenotazione);
    }


    public DtoPrenotazioneResponse map(Prenotazione p) {
        return new DtoPrenotazioneResponse(
                p.getId(),
                p.getData(),
                p.getOrarioInizio(),
                p.getOrarioFine(),
                p.getUtente().getId(),
                p.getUtente().getEmail(),
                p.getVeicolo().getId(),
                p.getVeicolo().getTarga(),
                p.getParcheggio().getId(),
                p.getParcheggio().getNumero()
        );
    }
}
