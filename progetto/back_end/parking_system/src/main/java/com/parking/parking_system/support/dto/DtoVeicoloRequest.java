package com.parking.parking_system.support.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DtoVeicoloRequest {

    @NotBlank(message = "La targa è obbligatoria")
    @Pattern(regexp = "^[A-Z]{2}[0-9]{3}[A-Z]{2}$",message = "La targa deve essere nel formato AA123AA")
    private String targa;

    @NotNull
    private Long utenteId;
}
