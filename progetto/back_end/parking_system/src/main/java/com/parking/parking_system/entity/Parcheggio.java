package com.parking.parking_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parcheggio")
@Getter
@Setter
@NoArgsConstructor
public class Parcheggio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false, unique = true)
    @Min(value = 1, message = "Il numero del parcheggio deve essere maggiore di 0")
    private int numero;

    @Column(name = "disponibile", nullable = false)
    private boolean disponibile = true;

    /*
     * Version serve se due utenti provano a prenotare lo stesso posto nello stesso momento,
     * Hibernate rileva che il record è stato modificato da un'altra transazione.
     */
    @Version
    private Long version;

    @OneToMany(mappedBy = "parcheggio")
    private List<Prenotazione> prenotazioni = new ArrayList<>();

    public void occupaParcheggio() {
        this.disponibile = false;
    }

    public void liberaParcheggio() {
        this.disponibile = true;
    }
}
