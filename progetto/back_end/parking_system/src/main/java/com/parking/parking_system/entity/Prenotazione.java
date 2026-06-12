package com.parking.parking_system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "prenotazione")
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;

    @Column(name = "orario_inizio")
    private LocalDateTime orarioInizio;

    @Column(name = "orario_fine")
    private LocalDateTime orarioFine;


    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;


    @ManyToOne
    @JoinColumn(name = "veicolo_id", nullable = false)
    private Veicolo veicolo;


    @ManyToOne
    @JoinColumn(name = "parcheggio_id", nullable = false)
    private Parcheggio parcheggio;
}