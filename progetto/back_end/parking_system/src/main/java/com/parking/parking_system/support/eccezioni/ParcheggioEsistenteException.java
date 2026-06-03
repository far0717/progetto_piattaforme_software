package com.parking.parking_system.support.eccezioni;

public class ParcheggioEsistenteException extends RuntimeException {
    public ParcheggioEsistenteException(int numero) {
            super("Parcheggio numero: " + numero + " ,già esiste");
    }

}
