package com.parking.parking_system.service;

import com.parking.parking_system.entity.Parcheggio;
import com.parking.parking_system.repository.ParcheggioRepository;
import com.parking.parking_system.support.eccezioni.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParcheggioService {
    private final ParcheggioRepository repo;

    //prenoto il parcheggio
    public Parcheggio prenotaParcheggio(int numero) {

        Parcheggio parcheggio = repo.findByNumero(numero);
        if (parcheggio == null) {
            throw new ParcheggioNotFoundException(numero);
        }
        if (!parcheggio.isDisponibile()) {
            throw new ParcheggioOccupatoException(numero);
        }

        parcheggio.occupaParcheggio();
        return repo.save(parcheggio);
    }//prenota()

    // Libera il parcheggio
    public Parcheggio liberaParcheggio(int numero) {
        Parcheggio parcheggio = repo.findByNumero(numero);

        if (parcheggio == null) {
            throw new ParcheggioNotFoundException(numero);
        }
        if (parcheggio.isDisponibile()) {
            throw new ParcheggioLiberoException(numero);
        }

        parcheggio.liberaParcheggio();

        // salva nel DB e restituisce l'oggetto aggiornato
        return repo.save(parcheggio);
    }//libera()

    //prendo tutti i parcheggi esistenti nel DB
    public List<Parcheggio> getAll() {
        return repo.findAll();
    }//getALL()

    //getParcheggioById
    public Parcheggio getById(Long id) {
        Parcheggio parcheggio = repo.findParcheggioById(id);
        if (parcheggio == null) {
            throw new ParcheggioNotFoundException(id);
        }
        return parcheggio;
    }//getById()

    public Parcheggio creaParcheggio(Parcheggio parcheggio) {
        if (repo.findByNumero(parcheggio.getNumero()) != null) {
            throw new ParcheggioEsistenteException(parcheggio.getNumero());
        }
        return repo.save(parcheggio);
    }//crea()

    public void eliminaParcheggio(Long id) {
        Parcheggio parcheggio = repo.findParcheggioById(id);
        if (parcheggio == null) {
            throw new ParcheggioNotFoundException(id);
        }
        repo.deleteById(id);
    }//elimina()
}
