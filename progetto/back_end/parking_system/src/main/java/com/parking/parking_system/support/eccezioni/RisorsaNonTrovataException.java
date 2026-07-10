package com.parking.parking_system.support.eccezioni;

public class RisorsaNonTrovataException extends RuntimeException {
    public RisorsaNonTrovataException(String message) {
        super(message);
    }
}
