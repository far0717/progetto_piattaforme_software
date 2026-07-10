package com.parking.parking_system.service;

import com.parking.parking_system.entity.Parcheggio;
import com.parking.parking_system.repository.ParcheggioRepository;
import com.parking.parking_system.repository.PrenotazioneRepository;
import com.parking.parking_system.support.dto.DtoParcheggioRequest;
import com.parking.parking_system.support.dto.DtoParcheggioResponse;
import com.parking.parking_system.support.eccezioni.*;
import com.parking.parking_system.support.validation.PrenotazioneValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParcheggioService {

    private final ParcheggioRepository repo;
    private final PrenotazioneRepository prenotazioneRepository;
    private final PrenotazioneValidator prenotazioneValidator;

    public List<DtoParcheggioResponse> getAll() {
        return repo.findAllByOrderByNumeroAsc().stream().map(this::map).toList();
    }

    public DtoParcheggioResponse getById(Long id) {
        return map(getParcheggioEntity(id));
    }

    public List<DtoParcheggioResponse> getDisponibili() {
        return repo.findByDisponibileTrueOrderByNumeroAsc().stream().map(this::map).toList();
    }

    public List<DtoParcheggioResponse> getDisponibilitaPerFascia(LocalDateTime orarioInizio,
                                                                 LocalDateTime orarioFine) {
        prenotazioneValidator.validaIntervallo(orarioInizio, orarioFine);

        return repo.findAllByOrderByNumeroAsc().stream()
                .map(parcheggio -> mapConDisponibilitaTemporale(parcheggio, orarioInizio, orarioFine))
                .toList();
    }

    @Transactional
    public DtoParcheggioResponse creaParcheggio(DtoParcheggioRequest request) {
        if (repo.existsByNumero(request.numero())) {
            throw new ParcheggioEsistenteException(request.numero());
        }

        Parcheggio parcheggio = new Parcheggio();
        parcheggio.setNumero(request.numero());
        parcheggio.setDisponibile(request.disponibile());
        return map(repo.save(parcheggio));
    }

    @Transactional
    public DtoParcheggioResponse aggiornaParcheggio(Long id, DtoParcheggioRequest request) {
        Parcheggio parcheggio = getParcheggioEntity(id);

        repo.findByNumero(request.numero()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new ParcheggioEsistenteException(request.numero());
            }
        });

        parcheggio.setNumero(request.numero());
        parcheggio.setDisponibile(request.disponibile());
        return map(repo.save(parcheggio));
    }

    @Transactional
    public void eliminaParcheggio(Long id) {
        Parcheggio parcheggio = getParcheggioEntity(id);
        if (prenotazioneRepository.existsByParcheggio(parcheggio)) {
            throw new OperazioneNonConsentitaException(
                    "Non puoi eliminare un parcheggio che ha prenotazioni associate");
        }
        repo.delete(parcheggio);
    }

    @Transactional
    public DtoParcheggioResponse disabilitaParcheggio(int numero) {
        Parcheggio parcheggio = repo.findByNumero(numero).orElseThrow(() -> new ParcheggioNotFoundException(numero));
        if (!parcheggio.isDisponibile()) {
            throw new ParcheggioOccupatoException(numero);
        }
        parcheggio.occupaParcheggio();
        return map(repo.save(parcheggio));
    }

    @Transactional
    public DtoParcheggioResponse abilitaParcheggio(int numero) {
        Parcheggio parcheggio = repo.findByNumero(numero).orElseThrow(() -> new ParcheggioNotFoundException(numero));
        if (parcheggio.isDisponibile()) {
            throw new ParcheggioLiberoException(numero);
        }
        parcheggio.liberaParcheggio();
        return map(repo.save(parcheggio));
    }

    private Parcheggio getParcheggioEntity(Long id) {
        return repo.findById(id).orElseThrow(() -> new ParcheggioNotFoundException(id));
    }

    private DtoParcheggioResponse mapConDisponibilitaTemporale(Parcheggio parcheggio,
                                                               LocalDateTime orarioInizio,
                                                               LocalDateTime orarioFine) {
        boolean liberoNellaFascia = parcheggio.isDisponibile()
                && !prenotazioneRepository.existsSovrapposizioneParcheggio(
                parcheggio.getId(), orarioInizio, orarioFine);

        return new DtoParcheggioResponse(parcheggio.getId(), parcheggio.getNumero(), liberoNellaFascia);
    }

    public DtoParcheggioResponse map(Parcheggio p) {
        return new DtoParcheggioResponse(p.getId(), p.getNumero(), p.isDisponibile());
    }
}
