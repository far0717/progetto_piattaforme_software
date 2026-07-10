package com.parking.parking_system.support.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DtoProfiloUtenteRequest(
        @NotBlank(message = "Il codice fiscale è obbligatorio")
        @Pattern(regexp = "(?i)^[A-Z]{6}[0-9]{2}[A-EHLMPRST][0-9]{2}[A-Z][0-9]{3}[A-Z]$",
                message = "Il codice fiscale deve contenere 16 caratteri nel formato corretto")
        String codiceFiscale,

        @NotBlank(message = "Il nome è obbligatorio")
        @Size(min = 2, max = 50, message = "Il nome deve avere tra 2 e 50 caratteri")
        String nome,

        @NotBlank(message = "Il cognome è obbligatorio")
        @Size(min = 2, max = 50, message = "Il cognome deve avere tra 2 e 50 caratteri")
        String cognome
) {}
