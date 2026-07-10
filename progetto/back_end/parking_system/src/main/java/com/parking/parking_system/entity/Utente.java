package com.parking.parking_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "utente")
@Getter
@Setter
@NoArgsConstructor//genera costruttore vuoto,JPA richiede un costruttore vuoto per poter creare
// l’oggetto quando legge una riga dal database.
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//Si usa Long e non long, perché prima del salvataggio l’id può essere null.


    //ho deciso di usare keycloak
    @NotBlank
    @Column(name = "keycloak_id", nullable = false, unique = true, length = 100)
    private String keycloakId;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{6}[0-9]{2}[A-EHLMPRST][0-9]{2}[A-Z][0-9]{3}[A-Z]$",
            message = "Il codice fiscale deve avere un formato valido")
    @Column(name = "codice_fiscale", nullable = false, length = 16, unique = true)
    private String codiceFiscale;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(name = "cognome", nullable = false, length = 50)
    private String cognome;

    @NotBlank
    @Email
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;


    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Veicolo> veicoli = new ArrayList<>();
    //cascade = CascadeType.ALL,propaga le operazioni dall’utente ai veicoli,se per esempio elimino
    //un utente, possono essere eliminati anche i suoi veicoli.
    //orphanRemoval = true,se un veicolo viene rimosso dalla lista dell’utente,
    // Hibernate può eliminarlo dal database.

    @OneToMany(mappedBy = "utente")
    private List<Prenotazione> prenotazioni = new ArrayList<>();
}
