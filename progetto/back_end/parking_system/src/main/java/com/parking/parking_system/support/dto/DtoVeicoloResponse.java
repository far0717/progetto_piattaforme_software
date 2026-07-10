package com.parking.parking_system.support.dto;

public record DtoVeicoloResponse(
        Long id,
        String targa,
        String marca,
        String modello,
        Long utenteId
) {}
