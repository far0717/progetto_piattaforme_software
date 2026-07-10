package com.parking.parking_system.support.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DtoPrenotazioneResponse(
        Long id,
        LocalDate data,
        LocalDateTime orarioInizio,
        LocalDateTime orarioFine,
        Long utenteId,
        String utenteEmail,
        Long veicoloId,
        String targa,
        Long parcheggioId,
        int numeroParcheggio
) {}
