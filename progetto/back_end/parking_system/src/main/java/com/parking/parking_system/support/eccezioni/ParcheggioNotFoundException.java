package com.parking.parking_system.support.eccezioni;

public class ParcheggioNotFoundException extends RisorsaNonTrovataException {
    public ParcheggioNotFoundException(int numero) {
        super("Parcheggio numero " + numero + " non trovato");
    }

    public ParcheggioNotFoundException(Long id) {
        super("Parcheggio con id " + id + " non trovato");
    }
}
