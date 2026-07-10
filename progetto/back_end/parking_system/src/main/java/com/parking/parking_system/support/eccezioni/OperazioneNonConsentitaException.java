package com.parking.parking_system.support.eccezioni;

public class OperazioneNonConsentitaException extends RuntimeException {
    public OperazioneNonConsentitaException(String message) {
        super(message);
    }
}
