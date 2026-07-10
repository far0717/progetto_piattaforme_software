package com.parking.parking_system.support.dto;

public record DtoUtenteResponse(
        Long id,
        String keycloakId,
        String codiceFiscale,
        String nome,
        String cognome,
        String email
) {}
