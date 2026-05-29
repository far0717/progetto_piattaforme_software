package com.parking.parking_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;


@Entity
@Table(name="utente")
public class Utente {
    //uso il Field access, ovvero metto le annotazioni sugli attributi,
    //NON uso il property access, ovvero mettere le annotazioni sui getter
    //con il Field access, Hibernate(il providere JPA)

    @Id
    //nella generazione automatica dell'id, indicando .IDENTITY nella strategia,
    //indico che l'id viene generato automaticamente dal DB
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //l'annotazione @Column non servirebbe in questo caso, pk Spring matcha in automatico
    //il nome della colonna nel DB,con lo stesso nome ma in camelCase stile Java
    /*
    l'annotazione @Column controlla come i dati vengono salvati del DB, invece le annotazioni di validazione come:
    @Email,@NotNull,@NotBlank,@Size controllano i dati in ingresso a livello Spring, quindi prima che arrivino al DB
    quindi se per esempio arriva una richiesta errata, grazie alle annotazioni di validazione,l'errore viene segnalato
    prima di arrivare al DB.
    @NotNull controlla che il campo non sia null, però permette campo vuoto, come : "" oppure " "
    @NotBlank controlla che il campo sia not null,che non sia vuoto quindi blocca "" e " ", più forte di @NotNull,
    funziona come per tipo String
    @NotEmpty,meno forte di @NotBlank controlla che il campo non sia null o vuoto,
    sono permessi spazi, funziona per i tipi:String,Collection,Map,Array
    @Size controlla la lunghezza,es. @Size(min = 3,max=50)
    @Email controlla che il formato del campo sia valido per una email,ma non controlla se il campo è vuoto,null,
    o se l'email esiste realmente
    @Pattern controlla se il formato rispetta la regex che indico
    @Min(numero) e @Max(numero) controllano il range numerico
    @Positive,@PositiveOrZero,@Negative,@NegativeOrZero, sostituiscono i simboli di >0,>=0,<0,=< 0

    DISCLAIMER
    Le annotazioni di validation non funzionano da sole, servono @Valid o Validator esplicito per attivarle,
    per esempio se voglio attivare le annotazioni di validazione, se passo in un metodo del Controller un oggetto Utente,
    allora lo annoto con @Valid, es. public void salva(@Valid Utente u), in questo modo le annotazioni dell' Entity Utente
    un altro modo per attivare le annotazioni è annotare il Service con @Validated e annotare @Valid Utente,
    un altro modo è la validazione manuale tramite un oggetto Validator, non penso di usarla.
     */
    @NotBlank
    @Pattern(regexp = "^[A-Z]{6}[0-9]{2}[A-EHLMPRST][0-9]{2}[A-Z][0-9]{3}[A-Z]$")
    @Column(name="codice_fiscale",nullable = false,length = 16 )
    private String codiceFiscale;

    @NotEmpty
    @Column(name="nome",nullable = false,length = 50)
    private String nome;
    @NotEmpty
    @Column(name="cognome",nullable = false,length = 50)
    private String cognome;
    @NotBlank
    @Email//verifica che l'email abbia il formato corretto,ma non che esista
    @Column(name = "email",nullable = false,length = 100)
    private String email;

    @NotBlank
    //nella regex della passoword ho messo che deve esserci almeno un numero, almeno un lettera minuscola,
    //almeno una lettera maiuscola,almeno un simbolo speciale, ed almeno 8 caratteri
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$")
    @Column(name = "password",nullable = false,length = 100)
    private String password;

    @Column(name = "abbonato",nullable = false)
    private boolean abbonato;




}
