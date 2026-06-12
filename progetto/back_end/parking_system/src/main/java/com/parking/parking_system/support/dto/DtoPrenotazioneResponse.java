package com.parking.parking_system.support.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DtoPrenotazioneResponse {

    private Long id;

    private LocalDate data;

    private LocalDateTime orarioInizio;

    private LocalDateTime orarioFine;

    private Long utenteId;

    private Long veicoloId;

    private Long parcheggioId;
}