package com.parking.parking_system.controller;

import com.parking.parking_system.entity.Utente;
import com.parking.parking_system.service.UtenteService;
import com.parking.parking_system.support.dto.DtoUtenteRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/utenti")
@RequiredArgsConstructor
public class UtenteController {
    //@AutoWired
    //Con @AutoWired chiedo a Spring di iniettare un bean(@Component,@Service,ecc.) già gestito dal container IoC,
    //risolvendo automaticamente la dependency injection. Non devo fare la new.
    //uso lombok.RequiredArgsConstructor pk mi permette di avere il campo final,
    // se manca una dipendenza, Spring fallisce subito all’avvio.
    private final UtenteService service;



    @PostMapping("/registra")
    //con @Valid valido i campi del DTO annotati con annotazioni di validazione come:@Email,@Size ...
    //non valido però i campi della Entity Utente,anche senza validazione le annotazioni JPA funzionano sempre
    //come @Entity,@Table,@Column,@Id,@GeneratedValue, se volessi validare le altre notazioni dovrei
    //validarle manualmente nel UtenteService con un oggetto Validate e annotando il Service con @Validated
    public Utente registra(@Valid @RequestBody DtoUtenteRequest request) {

        return service.registra(request);
    }
}