package com.parking.parking_system.controller;

import com.parking.parking_system.service.PrenotazioneService;
import com.parking.parking_system.support.dto.DtoPrenotazioneRequest;
import com.parking.parking_system.support.dto.DtoPrenotazioneResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prenotazioni")
@RequiredArgsConstructor
public class PrenotazioneController {

    private final PrenotazioneService service;

    @GetMapping
    public List<DtoPrenotazioneResponse> getMiePrenotazioni(@AuthenticationPrincipal Jwt jwt) {
        return service.getMiePrenotazioni(jwt);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public List<DtoPrenotazioneResponse> getAll() {
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DtoPrenotazioneResponse creaPrenotazione(@AuthenticationPrincipal Jwt jwt,
                                                    @Valid @RequestBody DtoPrenotazioneRequest request) {
        return service.creaPrenotazione(jwt, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancellaMiaPrenotazione(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        service.cancellaMiaPrenotazione(jwt, id);
    }
}
