package com.parking.parking_system.support.eccezioni;

public class ParcheggioOccupatoException extends OperazioneNonConsentitaException {
    public ParcheggioOccupatoException(int numero) {
        super("Parcheggio numero " + numero + " già occupato");
    }
}
