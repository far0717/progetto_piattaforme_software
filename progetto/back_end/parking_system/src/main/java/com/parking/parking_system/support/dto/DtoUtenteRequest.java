package com.parking.parking_system.support.dto;
//Lombock è una libreria esterna a Spring,ma integrata, che mi permette di evitare di scrivere i metodi getter
//setter,costruttori a mano, ma li crea lui in fase di eseczione.
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data; //@Data genera getter,setter,toString,equals,hashCode

import jakarta.validation.constraints.*;

/*
un DTO è un Data Transfer Object, lo uso per evitare di toccare direttamente l'Entity con i dati che ricevo
dal Front-end, quindi il Front-end manda i dati, il Back-end(SpringBoot) li riceve e anzichè usare direttamente
l'Entity uso il DTO, i controlli con le regex li faccio nel DTO non nell'Entity, questo per quelli che poi
vengono codificati come per esempio la password, per il codice fiscale faccio il controllo sia nel DTO che nella
Entity per sicurezza,questo avviene solo se valido l'Entity nel Service, altrimenti la validazione avviene solo nel DTO
*/
//@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoUtenteRequest {

    @NotBlank
    @Pattern(regexp = "^[A-Z]{6}[0-9]{2}[A-EHLMPRST][0-9]{2}[A-Z][0-9]{3}[A-Z]$")
    private String codiceFiscale;

    @NotBlank
    private String nome;

    @NotBlank
    private String cognome;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    //faccio il controllo tramite regex, che il client inserisca una password che rispetti il formato
    //nella regex della passoword ho messo che deve esserci almeno un numero, almeno un lettera minuscola,
    //almeno una lettera maiuscola,almeno un simbolo speciale, ed almeno 8 caratteri
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$")
    private String password;

    private boolean abbonato;
}