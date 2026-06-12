package com.parking.parking_system.support.dto;
//Lombock è una libreria esterna a Spring,ma integrata, che mi permette di evitare di scrivere i metodi getter
//setter,costruttori a mano, ma li crea lui in fase di eseczione.
import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
//import lombok.Data; //@Data genera getter,setter,toString,equals,hashCode

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
@NoArgsConstructor
@AllArgsConstructor
public class DtoUtenteRequest {

    /* Il message viene mostrato quando la validazione fallisce,
    Spring esegue la validazione grazie a @Valid nel controller,
    se la validazione fallisce allora parte un JSON verso l utente contenente il message
     */
    @NotBlank(message = "Il campo codice fiscale è obbligatorio")
    @Pattern(regexp = "^[A-Z]{6}[0-9]{2}[A-EHLMPRST][0-9]{2}[A-Z][0-9]{3}[A-Z]$",
    message = "Il codice fiscale deve contenere 16 caratteri nel formato corretto.")
    private String codiceFiscale;

    @NotBlank(message = "Il campo nome è obbligatorio")
    private String nome;

    @NotBlank(message = "Il campo cognome è obbligatorio")
    private String cognome;

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Inserisci un'email valida")
    private String email;

    @NotBlank(message = "il campo password è obbligatorio")
    //faccio il controllo tramite regex, che il client inserisca una password che rispetti il formato
    //nella regex della passoword ho messo che deve esserci almeno un numero, almeno un lettera minuscola,
    //almeno una lettera maiuscola,almeno un simbolo speciale, ed almeno 8 caratteri
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
    message = "la password deve essere lunga almeno 8 caratteri,almeno una lettera Maiuscola,almeno una lettera minuscola,almeno una carattere speciale")
    private String password;

    private boolean abbonato;
}