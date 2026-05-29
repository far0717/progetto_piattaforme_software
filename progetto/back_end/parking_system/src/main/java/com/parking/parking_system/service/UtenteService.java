package com.parking.parking_system.service;

import com.parking.parking_system.support.dto.DtoUtenteRequest;
import com.parking.parking_system.entity.Utente;
import com.parking.parking_system.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//validazione Entity Utente
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import java.util.Set;

//@Validated
@Service
@RequiredArgsConstructor //uso lombok.RequiredArgsConstructor anzichè @Autowired sopra i campi pk
// con @Autowired non posso mettere gli attributi final, ovvero non posso avere immutabilità
/*
Con Lombok posso evitare di scrivere a mano il costruttore, inoltre il Constructor injection che uso con lombok
è meglio di field injection + @Autowired, perché il costructor injection rende le dipendenze esplicite, immutabili e testabili,
Se manca una dipendenza, Spring fallisce subito all’avvio.
Se non usassi Lombok e dovessi scrivere più costruttori,
poi solo uno di essi può essere usato da Spring per la dependency injection e quello va annotato con @Autowired.
Invece quando uso il Service nei Controller e gli passo dei parametri, Lombok genera a compile time il costruttore.
*/

public class UtenteService {

    private final UtenteRepository repo;
    private final PasswordEncoder encoder;
    //private final Validator validator;


    public Utente registra(DtoUtenteRequest dto) {

        if (repo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email già registrata");
        }

        //assegno all'Entity Utente i dati, codifico la password
        Utente utente = new Utente();
        utente.setCodiceFiscale(dto.getCodiceFiscale());
        utente.setNome(dto.getNome());
        utente.setCognome(dto.getCognome());
        utente.setEmail(dto.getEmail());

        //qui codifico la password
        utente.setPassword(encoder.encode(dto.getPassword()));

        utente.setAbbonato(dto.isAbbonato());
        /*per validare l'Entity dovrei fare :
        Set<ConstraintViolation<Utente>> errors=validator.validate(utente);

        if (!errors.isEmpty()) {
            throw new RuntimeException(errors.toString());
        }
        */

        return repo.save(utente);
    }
}