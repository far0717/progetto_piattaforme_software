package com.parking.parking_system.service;

import com.parking.parking_system.entity.Parcheggio;
import com.parking.parking_system.repository.ParcheggioRepository;
import com.parking.parking_system.support.dto.DtoParcheggioResponse;
import com.parking.parking_system.support.eccezioni.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParcheggioService {
    private final ParcheggioRepository repo;

    //prenoto il parcheggio
    public DtoParcheggioResponse prenotaParcheggio(int numero) {

        Parcheggio parcheggio = repo.findByNumero(numero);
        if (parcheggio == null)
            throw new ParcheggioNotFoundException(numero);
        if (!parcheggio.isDisponibile())
            throw new ParcheggioOccupatoException(numero);
        parcheggio.occupaParcheggio();
        return map(repo.save(parcheggio));
    }//prenotaParcheggio()

    // Libera il parcheggio
    public DtoParcheggioResponse liberaParcheggio(int numero) {
        Parcheggio parcheggio = repo.findByNumero(numero);
        if (parcheggio == null)
            throw new ParcheggioNotFoundException(numero);
        if (parcheggio.isDisponibile())
            throw new ParcheggioLiberoException(numero);
        parcheggio.liberaParcheggio();
        return map(repo.save(parcheggio));
    }//liberaParcheggio()

    //prendo tutti i parcheggi esistenti nel DB
    public List<DtoParcheggioResponse> getAll() {
        return repo.findAll()
                .stream()
                .map(this::map)
                .toList();
    }//getALL()

    //getParcheggioById
    public DtoParcheggioResponse getById(Long id) {
        Parcheggio parcheggio = repo.findParcheggioById(id);
        if (parcheggio == null)
            throw new ParcheggioNotFoundException(id);
        return map(parcheggio);
    }//getById()

    /*
    public Parcheggio creaParcheggio(Parcheggio parcheggio) {
        if (repo.findByNumero(parcheggio.getNumero()) != null) {
            throw new ParcheggioEsistenteException(parcheggio.getNumero());
        }
        return repo.save(parcheggio);
    }//creaParcheggio()

    public void eliminaParcheggio(Long id) {
        Parcheggio parcheggio = repo.findParcheggioById(id);
        if (parcheggio == null) {
            throw new ParcheggioNotFoundException(id);
        }
        repo.deleteById(id);
    }//eliminaParcheggio()

     */

    public List<DtoParcheggioResponse> getDisponibili() {

        return repo.findByDisponibileTrue()
                .stream()
                .map(this::map)//(this::map) equivale a (p -> this.map(p))
                .toList();
    }//getDisponibili

    ///PRIVATE ///////////////////////////////////////////////////////
    private DtoParcheggioResponse map(Parcheggio p) {
        return new DtoParcheggioResponse(
                p.getId(),
                p.getNumero(),
                p.isDisponibile()
        );
    }

}
