package com.parking.parking_system.controller;

import com.parking.parking_system.entity.Parcheggio;
import com.parking.parking_system.service.ParcheggioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*ResponseEntity è la classe che dovrei usare per rendere il controller più preciso,
pk mi permette di essere preciso anche a livello HTTP,
senza ResponseEntity Spring restituisce sempre 200(OK) qualunque cosa accada,
*********AGGIUNGERE RESPONSE ENTITY NEI CONTROLLER********
 */

@RestController
@RequestMapping("/parcheggi")
@RequiredArgsConstructor
public class ParcheggioController {

    private final ParcheggioService service;

    //prendo tutti i parcheggi esistenti nel DB
    @GetMapping
    public List<Parcheggio> getAll() {
        return service.getAll();
    }

    //getParcheggioById
    @GetMapping("/{id}")
    public Parcheggio getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Parcheggio creaParcheggio(@Valid @RequestBody Parcheggio parcheggio) {
        return service.creaParcheggio(parcheggio);
    }

    @DeleteMapping("/{id}")
    public void eliminaParcheggio(@PathVariable Long id) {
        service.eliminaParcheggio(id);
    }
    //quando si fanno modifiche parziali si usa PATCH, fa parte di HTTP,
    //è un metodo di HTTP assieme a POST,PUT,GET,...
    //mettere PUT sarebbe sbagliato perchè implicherebbe che
    // sto sostituendo tutto il parcheggio e non solo un campo.
    @PatchMapping("/{numero}/prenota")
    public Parcheggio prenotaParcheggio(@PathVariable int numero) {
        return service.prenotaParcheggio(numero);
    }

    @PatchMapping("/{numero}/libera")
    public Parcheggio liberaParcheggio(@PathVariable int numero) {
        return service.liberaParcheggio(numero);
    }
}
