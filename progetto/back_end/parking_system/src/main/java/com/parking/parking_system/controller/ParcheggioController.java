package com.parking.parking_system.controller;

import com.parking.parking_system.service.ParcheggioService;
import com.parking.parking_system.support.dto.DtoParcheggioRequest;
import com.parking.parking_system.support.dto.DtoParcheggioResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/parcheggi")
@RequiredArgsConstructor
public class ParcheggioController {

    private final ParcheggioService service;

    @GetMapping
    public List<DtoParcheggioResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/disponibili")
    public List<DtoParcheggioResponse> getParcheggiDisponibili() {
        return service.getDisponibili();
    }

    @GetMapping("/disponibilita")
    public List<DtoParcheggioResponse> getDisponibilitaPerFascia(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime orarioInizio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime orarioFine
    ) {
        return service.getDisponibilitaPerFascia(orarioInizio, orarioFine);
    }

    @GetMapping("/{id}")
    public DtoParcheggioResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public DtoParcheggioResponse creaParcheggio(@Valid @RequestBody DtoParcheggioRequest request) {
        return service.creaParcheggio(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public DtoParcheggioResponse aggiornaParcheggio(@PathVariable Long id,
                                                    @Valid @RequestBody DtoParcheggioRequest request) {
        return service.aggiornaParcheggio(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminaParcheggio(@PathVariable Long id) {
        service.eliminaParcheggio(id);
    }


    @PatchMapping("/{numero}/disabilita")
    @PreAuthorize("hasRole('ADMIN')")
    public DtoParcheggioResponse disabilitaParcheggio(@PathVariable int numero) {
        return service.disabilitaParcheggio(numero);
    }

    @PatchMapping("/{numero}/abilita")
    @PreAuthorize("hasRole('ADMIN')")
    public DtoParcheggioResponse abilitaParcheggio(@PathVariable int numero) {
        return service.abilitaParcheggio(numero);
    }
}
