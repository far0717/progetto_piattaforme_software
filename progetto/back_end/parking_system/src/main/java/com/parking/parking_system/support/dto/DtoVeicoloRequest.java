package com.parking.parking_system.support.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DtoVeicoloRequest(
        @NotBlank(message = "La targa è obbligatoria")
        @Pattern(regexp = "(?i)^[A-Z]{2}[0-9]{3}[A-Z]{2}$", message = "La targa deve essere nel formato AA123AA")
        String targa,

        @NotBlank(message = "La marca è obbligatoria")
        @Size(max = 50)
        String marca,

        @NotBlank(message = "Il modello è obbligatorio")
        @Size(max = 50)
        String modello
) {}
