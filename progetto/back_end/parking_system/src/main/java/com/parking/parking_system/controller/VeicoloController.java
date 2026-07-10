package com.parking.parking_system.controller;

import com.parking.parking_system.service.VeicoloService;
import com.parking.parking_system.support.dto.DtoVeicoloRequest;
import com.parking.parking_system.support.dto.DtoVeicoloResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veicoli")
@RequiredArgsConstructor
public class VeicoloController {

    private final VeicoloService service;

    @GetMapping
    public List<DtoVeicoloResponse> getMieiVeicoli(@AuthenticationPrincipal Jwt jwt) {
        return service.getMieiVeicoli(jwt);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DtoVeicoloResponse creaVeicolo(@AuthenticationPrincipal Jwt jwt,
                                          @Valid @RequestBody DtoVeicoloRequest request) {
        return service.creaVeicolo(jwt, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminaMioVeicolo(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        service.eliminaMioVeicolo(jwt, id);
    }
}
