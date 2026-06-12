package com.parking.parking_system.support.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DtoParcheggioResponse {

    private Long id;
    private int numero;
    private boolean disponibile;
}