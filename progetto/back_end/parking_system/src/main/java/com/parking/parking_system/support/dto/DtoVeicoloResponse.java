package com.parking.parking_system.support.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DtoVeicoloResponse {

    private Long id;

    private String targa;

    private String marca;

    private String modello;
}