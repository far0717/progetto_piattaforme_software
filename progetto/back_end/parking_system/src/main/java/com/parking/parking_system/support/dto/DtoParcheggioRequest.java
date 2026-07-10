package com.parking.parking_system.support.dto;

import jakarta.validation.constraints.Min;

public record DtoParcheggioRequest(
        @Min(value = 1, message = "Il numero del parcheggio deve essere maggiore di 0")
        int numero,
        boolean disponibile
) {}
