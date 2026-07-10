package com.parking.parking_system.controller;

import com.parking.parking_system.service.UtenteService;
import com.parking.parking_system.support.dto.DtoProfiloUtenteRequest;
import com.parking.parking_system.support.dto.DtoUtenteResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/utenti")
@RequiredArgsConstructor
public class UtenteController {

    private final UtenteService service;

    @GetMapping("/me")
    public DtoUtenteResponse getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        return service.getMyProfile(jwt);
    }

    @PostMapping("/me")
    @ResponseStatus(HttpStatus.CREATED)
    public DtoUtenteResponse createOrUpdateMyProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody DtoProfiloUtenteRequest request
    ) {
        return service.createOrUpdateMyProfile(jwt, request);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<DtoUtenteResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public DtoUtenteResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }
}
