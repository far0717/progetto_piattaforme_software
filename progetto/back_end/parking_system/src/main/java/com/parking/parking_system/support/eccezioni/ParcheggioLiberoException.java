package com.parking.parking_system.support.eccezioni;

public class ParcheggioLiberoException extends OperazioneNonConsentitaException {
    public ParcheggioLiberoException(int numero) {
        super("Parcheggio numero " + numero + " è già libero");
    }
}
