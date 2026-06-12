package com.parking.parking_system.support.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DtoUtenteResponse {

    private Long id;

    private String codiceFiscale;

    private String nome;

    private String cognome;

    private String email;

    private boolean abbonato;
}