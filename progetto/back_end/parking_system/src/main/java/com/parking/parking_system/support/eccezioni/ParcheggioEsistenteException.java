package com.parking.parking_system.support.eccezioni;

public class ParcheggioEsistenteException extends RisorsaDuplicataException {
    public ParcheggioEsistenteException(int numero) {
        super("Parcheggio numero " + numero + " già esistente");
    }
}
