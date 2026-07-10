package com.parking.parking_system.support.dto;

public record DtoParcheggioResponse(
        Long id,
        int numero,
        boolean disponibile
) {}
