package com.parking.parking_system.support.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DtoPrenotazioneRequest(
        @NotNull(message = "L'id del veicolo è obbligatorio")
        Long veicoloId,

        @NotNull(message = "L'id del parcheggio è obbligatorio")
        Long parcheggioId,

        @NotNull(message = "L'orario di inizio è obbligatorio")
        @FutureOrPresent(message = "L'orario di inizio non può essere nel passato")
        LocalDateTime orarioInizio,

        @NotNull(message = "L'orario di fine è obbligatorio")
        LocalDateTime orarioFine
) {}
